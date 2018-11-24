//
// Created by afirez on 24/11/2018.
//

#ifndef KNIGHT_ANDROID_LOG_H
#define KNIGHT_ANDROID_LOG_H

#include <android/log.h>

#define LOG_DEBUG true

#define LOG_TAG "Player"
#define LOGE(...) __android_log_print(ANDROID_LOG_ERROR, LOG_TAG, __VA_ARGS__)
#define LOGW(...) __android_log_print(ANDROID_LOG_WARN, LOG_TAG, __VA_ARGS__)
#define LOGI(...) __android_log_print(ANDROID_LOG_INFO, LOG_TAG, __VA_ARGS__)

#endif //KNIGHT_ANDROID_LOG_H
