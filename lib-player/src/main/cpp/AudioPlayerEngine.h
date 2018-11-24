//
// Created by afirez on 24/11/2018.
//

#ifndef KNIGHT_AUDIO_PLAYER_ENGINE_H
#define KNIGHT_AUDIO_PLAYER_ENGINE_H


#include <pthread.h>
#include "CallOnPrepared.h"
#include "AudioPlayerStatus.h"
#include "AudioFrameQueue.h"

extern "C" {
#include <libavformat/avformat.h>
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


public:
    AudioPlayerEngine(const char *url, CallOnPrepared *callOnPrepared);

    ~AudioPlayerEngine();

    void prepare();

    void prepareActually();

    void start();

};


#endif //KNIGHT_AUDIO_PLAYER_ENGINE_H
