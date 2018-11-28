//
// Created by afirez on 24/11/2018.
//

#include "AudioFrameQueue.h"

AudioFrameQueue::AudioFrameQueue(AudioPlayerStatus *status) {
    pthread_mutex_init(&mutex, NULL);
    pthread_cond_init(&cond, NULL);
    this->status = status;
}

AudioFrameQueue::~AudioFrameQueue() {
    pthread_mutex_destroy(&mutex);
    pthread_cond_destroy(&cond);
}

int AudioFrameQueue::pop(AVPacket *packet) {
    pthread_mutex_lock(&mutex);

    while (status != NULL && !status->exit) {
        if (frameQueue.size() <= 0) {
            pthread_cond_wait(&cond, &mutex);
            continue;
        }

        AVPacket *thePacket = frameQueue.front();
        if (av_packet_ref(packet, thePacket) == 0) {
            frameQueue.pop();
        }

        av_packet_free(&thePacket);

        if (LOG_DEBUG) {
            LOGI("Audio frame queue pop: %d", frameQueue.size());
        }

        break;
    }

    pthread_mutex_unlock(&mutex);

    return 0;
}

int AudioFrameQueue::push(AVPacket *packet) {
    pthread_mutex_lock(&mutex);

    frameQueue.push(packet);

    if (LOG_DEBUG) {
        LOGI("Audio frame Queue push: size == %d", frameQueue.size());
    }

    pthread_cond_signal(&cond);
    pthread_mutex_unlock(&mutex);
    return 0;
}

int AudioFrameQueue::size() {
    int size = 0;
    pthread_mutex_lock(&mutex);
    size = frameQueue.size();
    pthread_mutex_unlock(&mutex);
    return size;
}


