//
// Created by danbogh on 2017/5/10.
//

#ifndef OPENGLES_GL_UTILS_H
#define OPENGLES_GL_UTILS_H

#include <stdlib.h>

#include <GLES3/gl3.h>

#include <jni.h>

#include <android/log.h>
#ifndef LOG_TAG
#define LOG_TAG "OpenGL"
#endif
#define LOGE(...) __android_log_print(ANDROID_LOG_ERROR, LOG_TAG, __VA_ARGS__)
#define LOGW(...) __android_log_print(ANDROID_LOG_WANNAR, LOG_TAG, __VA_ARGS__)

#define LOGI(...) __android_log_print(ANDROID_LOG_INFO, LOG_TAG, __VA_ARGS__)

//检查当前程序错误
int checkGlError(const char *funcName);

//获取并编译着色器对象
GLuint createShader(GLenum shaderType, const char *src);

//使用着色器生成着色器程序对象
GLuint createProgram(const char *vertexSrc, const char *fragmentSrc);

#endif //OPENGLES_GL_UTILS_H
