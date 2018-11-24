//
// Created by afirez on 24/11/2018.
//

#include "AudioPlayerEngine.h"


AudioPlayerEngine::AudioPlayerEngine(const char *url, CallOnPrepared *callOnPrepared) {
    this->url = url;
    this->callOnPrepared = callOnPrepared;
}

void *prepareAction(void *data) {
    AudioPlayerEngine *playerEngine = (AudioPlayerEngine *) data;
    playerEngine->prepareActually();
    pthread_exit(&playerEngine->decodeThread);
}

void AudioPlayerEngine::prepare() {
    pthread_create(&decodeThread, NULL, prepareAction, this);
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
            this->streamIndex = i;
            this->stream = formatContext->streams[i];
        }
    }

    this->codec = avcodec_find_decoder(this->stream->codecpar->codec_id);

    if (this->codec == NULL) {
        if (LOG_DEBUG) {
            LOGE("can not find decoder");
        }
        return;
    }

    this->codecContext = avcodec_alloc_context3(codec);
    if (this->codecContext == NULL) {
        if (LOG_DEBUG) {
            LOGE("can not create codec context");
        }
        return;
    }

    if (avcodec_parameters_to_context(this->codecContext, this->stream->codecpar) < 0) {
        if (LOG_DEBUG) {
            LOGE("can not fill codec parameters to codec context");
        }
        return;
    }

    if (avcodec_open2(this->codecContext, this->codec, NULL) != 0) {
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
            av_free(packet);
            break;
        }

        if (packet->stream_index != this->streamIndex) {
            av_packet_free(&packet);
            av_free(packet);
            continue;
        }

        count++;
        LOGI("decoded frame: %d", count);
        av_packet_free(&packet);
        av_free(packet);
    }
}
