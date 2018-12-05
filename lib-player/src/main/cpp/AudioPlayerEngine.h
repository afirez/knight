//
// Created by afirez on 24/11/2018.
//

#ifndef KNIGHT_AUDIO_PLAYER_ENGINE_H
#define KNIGHT_AUDIO_PLAYER_ENGINE_H


#include <pthread.h>
#include "CallOnPrepared.h"
#include "AudioPlayerStatus.h"
#include "AudioFrameQueue.h"

#include <SLES/OpenSLES.h>
#include <SLES/OpenSLES_Android.h>

extern "C" {
#include <libavformat/avformat.h>
#include <libswresample/swresample.h>
};

class AudioPlayerEngine {
public:
    const char *url;

    CallOnPrepared *callOnPrepared;

    AudioPlayerStatus *status;

    pthread_t audioDecodeThread;

    AVFormatContext *formatContext = NULL;

    int audioStreamIndex = -1;
    AVStream *audioStream  = NULL;

    AVCodec *audioCodec = NULL;
    AVCodecContext *audioCodecContext = NULL;

    AudioFrameQueue *audioFrameQueue;

    pthread_t audioPlayThread;

    AVPacket *packet = NULL;
    AVFrame *frame = NULL;
    uint8_t *buffer = NULL;
    int dataSize = 0;

    SLObjectItf slObj = NULL;
    SLEngineItf slEngine = NULL;

    SLObjectItf outputMixObj = NULL;
    SLEnvironmentalReverbItf outputEnvironmentalReverb = NULL;
    SLEnvironmentalReverbSettings reverbSettings = SL_I3DL2_ENVIRONMENT_PRESET_STONECORRIDOR;

    SLObjectItf pcmPlayerObj = NULL;
    SLPlayItf pcmPlayerPlay = NULL;
    SLAndroidSimpleBufferQueueItf pcmBufferQueue = NULL;

public:
    AudioPlayerEngine(const char *url, CallOnPrepared *callOnPrepared);

    ~AudioPlayerEngine();

    void prepare();

    void prepareActually();

    void start();

    int resamapleAudio();

    void play();

    void initAudioRenderer();
};


#endif //KNIGHT_AUDIO_PLAYER_ENGINE_H
