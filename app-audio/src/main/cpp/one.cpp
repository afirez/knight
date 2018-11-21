//
// Created by afirez on 21/11/2018.
//

#include <jni.h>

#include <android/log.h>

#define LOG_TAG "ONE"
#define LOGE(...) __android_log_print(ANDROID_LOG_ERROR, LOG_TAG, __VA_ARGS__)
#define LOGW(...) __android_log_print(ANDROID_LOG_WAN, LOG_TAG, __VA_ARGS__)
#define LOGI(...) __android_log_print(ANDROID_LOG_INFO, LOG_TAG, __VA_ARGS__)

//extern "C"
//JNIEXPORT jstring JNICALL
//Java_com_afirez_lib_player_One_apply(JNIEnv *env, jclass type) {
//    av_register_all();
//    AVCodec *p_codec = av_codec_next(NULL);
//
//    while (p_codec != NULL) {
//        switch (p_codec->type) {
//            case AVMEDIA_TYPE_VIDEO:
//                LOGI("[Video: %s]", p_codec->name);
//                break;
//            case AVMEDIA_TYPE_AUDIO:
//                LOGI("[Audio: %s]", p_codec->name);
//                break;
//            default:
//                LOGI("[Other: %s]", p_codec->name);
//        }
//
//    }
//
//    return env->NewStringUTF("Hello World");
//}