//
// Created by afirez on 24/11/2018.
//

#include "AudioPlayerEngine.h"


AudioPlayerEngine::AudioPlayerEngine(const char *url, CallOnPrepared *callOnPrepared) {
    this->url = url;
    this->callOnPrepared = callOnPrepared;
    this->status = new AudioPlayerStatus();

    this->audioFrameQueue = new AudioFrameQueue(this->status);
}

void *prepareAction(void *data) {
    AudioPlayerEngine *playerEngine = (AudioPlayerEngine *) data;
    playerEngine->prepareActually();
    pthread_exit(&playerEngine->audioDecodeThread);
}

void AudioPlayerEngine::prepare() {
    pthread_create(&audioDecodeThread, NULL, prepareAction, this);
}

void AudioPlayerEngine::prepareActually() {

    av_register_all();

    avformat_network_init();

    this->formatContext = avformat_alloc_context();

    if (avformat_open_input(&this->formatContext, this->url, NULL, NULL) != 0) {
        if (LOG_DEBUG) {
            LOGE("can not open url: %s", this->url);
        }
        return;
    }

    if (avformat_find_stream_info(formatContext, NULL) < 0) {
        if (LOG_DEBUG) {
            LOGE("can not find streams for url: %s", url);
        }
        return;
    }

    for (int i = 0; i < formatContext->nb_streams; ++i) {
        if (formatContext->streams[i]->codecpar->codec_type == AVMEDIA_TYPE_AUDIO) {
            this->audioStreamIndex = i;
            this->audioStream = formatContext->streams[i];
        }
    }

    this->audioCodec = avcodec_find_decoder(this->audioStream->codecpar->codec_id);

    if (this->audioCodec == NULL) {
        if (LOG_DEBUG) {
            LOGE("can not find decoder");
        }
        return;
    }

    this->audioCodecContext = avcodec_alloc_context3(audioCodec);
    if (this->audioCodecContext == NULL) {
        if (LOG_DEBUG) {
            LOGE("can not create codec context");
        }
        return;
    }

    if (avcodec_parameters_to_context(this->audioCodecContext, this->audioStream->codecpar) < 0) {
        if (LOG_DEBUG) {
            LOGE("can not fill codec parameters to codec context");
        }
        return;
    }

    if (avcodec_open2(this->audioCodecContext, this->audioCodec, NULL) != 0) {
        if (LOG_DEBUG) {
            LOGE("can not open codec");
        }
        return;
    }

    this->callOnPrepared->onPrepared(TYPE_WORKER_THREAD);
}

void AudioPlayerEngine::start() {
    int count = 0;

    while (1) {
        AVPacket *packet = av_packet_alloc();
        if (av_read_frame(this->formatContext, packet) != 0) {
            av_packet_free(&packet);
            break;
        }

        if (packet->stream_index != this->audioStreamIndex) {
            av_packet_free(&packet);
            continue;
        }

        count++;
        LOGI("decoded frame: %d", count);
        this->audioFrameQueue->push(packet);

//        av_packet_free(&packet);
    }


    //mock pop
    while(audioFrameQueue->size() > 0) {
        AVPacket *thePacket = av_packet_alloc();
        audioFrameQueue->pop(thePacket);
        av_packet_free(&thePacket);
        thePacket = NULL;
    }
}
