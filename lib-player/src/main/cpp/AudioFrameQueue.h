//
// Created by afirez on 24/11/2018.
//

#ifndef KNIGHT_AUDIOFRAMEQUEUE_H
#define KNIGHT_AUDIOFRAMEQUEUE_H

#include "queue"
#include "pthread.h"

#include "android_log.h"
#include "AudioPlayerStatus.h"

extern "C" {
#include <libavcodec/avcodec.h>
};

class AudioFrameQueue {
public:
    std::queue<AVPacket *> frameQueue;
    pthread_mutex_t mutex;
    pthread_cond_t cond;

    AudioPlayerStatus* status;

public:
    AudioFrameQueue(AudioPlayerStatus *status);
    ~AudioFrameQueue();

    int pop(AVPacket *packet);
    int push(AVPacket *packet);
    int size();

};

#endif //KNIGHT_AUDIOFRAMEQUEUE_H
