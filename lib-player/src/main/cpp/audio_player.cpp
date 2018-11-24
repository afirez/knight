//
// Created by afirez on 24/11/2018.
//

#include <jni.h>

#include "android_log.h"
#include "CallOnPrepared.h"
#include "AudioPlayerEngine.h"

JavaVM *javaVM;
CallOnPrepared *callOnPrepared;

AudioPlayerEngine *playerEngine;

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