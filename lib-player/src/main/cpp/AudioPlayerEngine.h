//
// Created by afirez on 24/11/2018.
//

#ifndef KNIGHT_AUDIO_PLAYER_ENGINE_H
#define KNIGHT_AUDIO_PLAYER_ENGINE_H


#include <pthread.h>
#include "CallOnPrepared.h"

extern "C" {
#include <libavformat/avformat.h>
};

class AudioPlayerEngine {
public:
    const char *url;

    CallOnPrepared *callOnPrepared;

    pthread_t decodeThread;

    AVFormatContext *formatContext = NULL;

    int streamIndex = -1;
    AVStream *stream  = NULL;

    AVCodec *codec = NULL;
    AVCodecContext *codecContext = NULL;

public:
    AudioPlayerEngine(const char *url, CallOnPrepared *callOnPrepared);

    ~AudioPlayerEngine();

    void prepare();

    void prepareActually();

    void start();

};


#endif //KNIGHT_AUDIO_PLAYER_ENGINE_H
