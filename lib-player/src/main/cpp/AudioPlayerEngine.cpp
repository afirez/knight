//
// Created by afirez on 24/11/2018.
//

#include "AudioPlayerEngine.h"


AudioPlayerEngine::AudioPlayerEngine(const char *url, CallOnPrepared *callOnPrepared) {
    this->url = url;
    this->callOnPrepared = callOnPrepared;
    this->status = new AudioPlayerStatus();

    this->audioFrameQueue = new AudioFrameQueue(this->status);

    buffer = (uint8_t *) (av_malloc(44100 * 2 * 2));
}

AudioPlayerEngine::~AudioPlayerEngine() {
    av_free(buffer);
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

    this->play();

    int count = 0;

    while (status != NULL && !status->exit) {
        AVPacket *packet = av_packet_alloc();
        if (av_read_frame(this->formatContext, packet) != 0) {
            av_packet_free(&packet);
            while (status != NULL && !status->exit) {
                if (audioFrameQueue->size() > 0) {
                    continue;
                } else {
                    status->exit = true;
                    break;
                }
            }
            break;
        }

        if (packet->stream_index != this->audioStreamIndex) {
            av_packet_free(&packet);
            continue;
        }

        count++;
        if (LOG_DEBUG) {
            LOGI("decoded frame: %d", count);
        }
        this->audioFrameQueue->push(packet);

//        av_packet_free(&packet);
    }

    if (LOG_DEBUG) {
        LOGI("Cheers");
    }

    //mock pop
//    while (audioFrameQueue->size() > 0) {
//        AVPacket *thePacket = av_packet_alloc();
//        audioFrameQueue->pop(thePacket);
//        av_packet_free(&thePacket);
//        thePacket = NULL;
//    }
}

void *decodePlay(void *data) {
    AudioPlayerEngine *engine = (AudioPlayerEngine *) data;
    engine->initAudioRenderer();
    pthread_exit(&engine->audioPlayThread);
}

int AudioPlayerEngine::resamapleAudio() {
    while (status != NULL && !status->exit) {

        packet = av_packet_alloc();
        if (audioFrameQueue->pop(packet) != 0) {
            av_packet_free(&packet);
            continue;
        }

        int code = avcodec_send_packet(audioCodecContext, packet);
        if (code != 0) {
            av_packet_free(&packet);
            continue;
        }

        frame = av_frame_alloc();
        code = avcodec_receive_frame(audioCodecContext, frame);
        if (code != 0) {
            av_packet_free(&packet);
            av_frame_free(&frame);
            continue;
        }

        if (frame->channels > 0 && frame->channel_layout == 0) {
            frame->channel_layout = (uint64_t) (av_get_default_channel_layout(frame->channels));
        } else if (frame->channels == 0 && frame->channel_layout > 0) {
            frame->channels = av_get_channel_layout_nb_channels(frame->channel_layout);
        }

        SwrContext *swrContext;
        swrContext = swr_alloc_set_opts(
                NULL,
                AV_CH_LAYOUT_STEREO,
                AV_SAMPLE_FMT_S16,
                frame->sample_rate,
                frame->channel_layout,
                (AVSampleFormat) (frame->format),
                frame->sample_rate,
                NULL,
                NULL
        );

        if (!swrContext || swr_init(swrContext) < 0) {
            av_packet_free(&packet);
            av_frame_free(&frame);
            if (!swrContext) {
                swr_free(&swrContext);
            }
            continue;
        }

        int nb = swr_convert(
                swrContext,
                &buffer,
                frame->nb_samples,
                (const uint8_t **) (frame->data),
                frame->nb_samples
        );

        int outChannels = av_get_channel_layout_nb_channels(AV_CH_LAYOUT_STEREO);
        dataSize = nb * outChannels * av_get_bytes_per_sample(AV_SAMPLE_FMT_S16);

        if (LOG_DEBUG) {
            LOGI("dataSize = %d", dataSize);
        }

        av_packet_free(&packet);
        av_frame_free(&frame);
        if (!swrContext) {
            swr_free(&swrContext);
        }
        break;
    }
    return dataSize;
}

void AudioPlayerEngine::play() {
    pthread_create(&audioPlayThread, NULL, decodePlay, this);
}

SLuint32 getCurrentSampleRateForOpensles(int sampleRate) {
    SLuint32 rate = 0;
    switch (sampleRate) {
        case 8000:
            rate = SL_SAMPLINGRATE_8;
            break;
        case 11025:
            rate = SL_SAMPLINGRATE_11_025;
            break;
        case 12000:
            rate = SL_SAMPLINGRATE_12;
            break;
        case 16000:
            rate = SL_SAMPLINGRATE_16;
            break;
        case 22050:
            rate = SL_SAMPLINGRATE_22_05;
            break;
        case 24000:
            rate = SL_SAMPLINGRATE_24;
            break;
        case 32000:
            rate = SL_SAMPLINGRATE_32;
            break;
        case 44100:
            rate = SL_SAMPLINGRATE_44_1;
            break;
        case 48000:
            rate = SL_SAMPLINGRATE_48;
            break;
        case 64000:
            rate = SL_SAMPLINGRATE_64;
            break;
        case 88200:
            rate = SL_SAMPLINGRATE_88_2;
            break;
        case 96000:
            rate = SL_SAMPLINGRATE_96;
            break;
        case 192000:
            rate = SL_SAMPLINGRATE_192;
            break;
        default:
            rate = SL_SAMPLINGRATE_44_1;
    }
    return rate;
}

void bufferCallback(SLAndroidSimpleBufferQueueItf bufferQueue, void *context) {
    AudioPlayerEngine *engine = (AudioPlayerEngine *) (context);
    int bufferSize = engine->resamapleAudio();
    if (bufferSize > 0) {
        (*engine->pcmBufferQueue)->Enqueue(engine->pcmBufferQueue, engine->buffer, (SLuint32) (bufferSize));
    }
}

void AudioPlayerEngine::initAudioRenderer() {
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
            getCurrentSampleRateForOpensles(this->audioStream->codecpar->sample_rate),
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

    (*pcmBufferQueue)->RegisterCallback(pcmBufferQueue, bufferCallback, this);

    (*pcmPlayerPlay)->SetPlayState(pcmPlayerPlay, SL_PLAYSTATE_PLAYING);

    bufferCallback(pcmBufferQueue, this);
}
