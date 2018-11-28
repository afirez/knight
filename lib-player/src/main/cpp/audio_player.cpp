//
// Created by afirez on 24/11/2018.
//

#include <jni.h>

#include "android_log.h"
#include "CallOnPrepared.h"
#include "AudioPlayerEngine.h"

#include <SLES/OpenSLES.h>
#include <SLES/OpenSLES_Android.h>

JavaVM *javaVM;
CallOnPrepared *callOnPrepared;

AudioPlayerStatus *status;

AudioPlayerEngine *playerEngine;


SLObjectItf slObj = NULL;
SLEngineItf slEngine = NULL;

SLObjectItf outputMixObj = NULL;
SLEnvironmentalReverbItf outputEnvironmentalReverb = NULL;
SLEnvironmentalReverbSettings reverbSettings = SL_I3DL2_ENVIRONMENT_PRESET_STONECORRIDOR;

SLObjectItf pcmPlayerObj = NULL;
SLPlayItf pcmPlayerPlay = NULL;
SLAndroidSimpleBufferQueueItf pcmBufferQueue = NULL;

FILE *pcmFile;
void *buffer;
uint8_t *out_buffer;

int readPcmData(void **pcm) {
    int size = 0;
    while (!feof(pcmFile)) {
        size = fread(out_buffer, 1, 44100 * 2 * 2, pcmFile);
        if (out_buffer == NULL) {
            if (LOG_DEBUG) {
                LOGI("read end");
            }
            break;
        } else {
            if (LOG_DEBUG) {
                LOGI("reading");
            }
        }
        *pcm = out_buffer;
        break;
    }
    return size;
}

void pcmBufferCallback(SLAndroidSimpleBufferQueueItf bufferQueue, void *context) {
    int size = readPcmData(&buffer);
    if (buffer != NULL) {
        (*pcmBufferQueue)->Enqueue(pcmBufferQueue, buffer, size);
    }
}

extern "C"
JNIEXPORT int JNICALL
JNI_OnLoad(JavaVM *vm, void *reserved) {
    javaVM = vm;
    JNIEnv *env;
    if (vm->GetEnv((void **) (&env), JNI_VERSION_1_6) != JNI_OK) {
        return -1;
    }

    return JNI_VERSION_1_6;
}

extern "C"
JNIEXPORT void JNICALL
Java_com_afirez_lib_player_AudioPlayer_prepareN(JNIEnv *env, jobject instance, jstring jurl) {
    const char *url = env->GetStringUTFChars(jurl, 0);

    if (playerEngine == NULL) {
        if (callOnPrepared == NULL) {
            callOnPrepared = new CallOnPrepared(javaVM, env, instance);
        }

        status = new AudioPlayerStatus();
        playerEngine = new AudioPlayerEngine(url, callOnPrepared);
    }
    playerEngine->prepare();

//    env->ReleaseStringUTFChars(jurl, url);
}

extern "C"
JNIEXPORT void JNICALL
Java_com_afirez_lib_player_AudioPlayer_startN(JNIEnv *env, jobject instance) {
    if (playerEngine != NULL) {
        playerEngine->start();
    }
}

extern "C"
JNIEXPORT void JNICALL
Java_com_afirez_lib_player_AudioPlayer_playPcmN(JNIEnv *env, jobject instance, jstring url_) {
    const char *url = env->GetStringUTFChars(url_, 0);

    pcmFile = fopen(url, "r");
    if (pcmFile == NULL) {
        LOGE("file open error");
        return;
    }

    out_buffer = (uint8_t *) malloc(44100 * 2 * 2);

    slCreateEngine(&slObj, 0, 0, 0, 0, 0);
    (*slObj)->Realize(slObj, SL_BOOLEAN_FALSE);
    (*slObj)->GetInterface(slObj, SL_IID_ENGINE, &slEngine);

    const SLInterfaceID mids[1] = {SL_IID_ENVIRONMENTALREVERB};
    const SLboolean mreq[1] = {SL_BOOLEAN_FALSE};

    (*slEngine)->CreateOutputMix(slEngine, &outputMixObj, 1, mids, mreq);
    (*outputMixObj)->Realize(outputMixObj, SL_BOOLEAN_FALSE);
    (*outputMixObj)->GetInterface(outputMixObj, SL_IID_ENVIRONMENTALREVERB, &outputEnvironmentalReverb);

    (*outputEnvironmentalReverb)->SetEnvironmentalReverbProperties(outputEnvironmentalReverb, &reverbSettings);

    SLDataLocator_AndroidBufferQueue androidQueue = {
            SL_DATALOCATOR_ANDROIDSIMPLEBUFFERQUEUE,
            2
    };
    SLDataFormat_PCM pcm = {
            SL_DATAFORMAT_PCM,
            2,
            SL_SAMPLINGRATE_44_1,
            SL_PCMSAMPLEFORMAT_FIXED_16,
            SL_PCMSAMPLEFORMAT_FIXED_16,
            SL_SPEAKER_FRONT_LEFT | SL_SPEAKER_FRONT_RIGHT,
            SL_BYTEORDER_LITTLEENDIAN
    };

    SLDataSource slDataSource = {&androidQueue, &pcm};

    SLDataLocator_OutputMix outputMix = {
            SL_DATALOCATOR_OUTPUTMIX,
            outputMixObj
    };
    SLDataSink audioSink = {
            &outputMix,
            NULL
    };

    const SLInterfaceID ids[1] = {SL_IID_BUFFERQUEUE};
    const SLboolean req[1] = {SL_BOOLEAN_TRUE};


    (*slEngine)->CreateAudioPlayer(
            slEngine,
            &pcmPlayerObj,
            &slDataSource,
            &audioSink,
            1,
            ids,
            req
    );
    (*pcmPlayerObj)->Realize(pcmPlayerObj, SL_BOOLEAN_FALSE);

    (*pcmPlayerObj)->GetInterface(pcmPlayerObj, SL_IID_PLAY, &pcmPlayerPlay);

    (*pcmPlayerObj)->GetInterface(pcmPlayerObj, SL_IID_BUFFERQUEUE, &pcmBufferQueue);

    (*pcmBufferQueue)->RegisterCallback(pcmBufferQueue, pcmBufferCallback, NULL);

    (*pcmPlayerPlay)->SetPlayState(pcmPlayerPlay, SL_PLAYSTATE_PLAYING);

    pcmBufferCallback(pcmBufferQueue, NULL);

    env->ReleaseStringUTFChars(url_, url);
}