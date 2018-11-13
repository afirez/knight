//
// Created by danbogh on 2017/5/10.
//
#ifndef OPENGLES_TRIANGLE_H
#define OPENGLES_TRIANGLE_H

#include <jni.h>

#include "gl_utils.h"

#include <android/log.h>

//#define LOG_TAG "TRIANGLE"
//#define LOGE(...) __android_log_print(ANDROID_LOG_ERROR, LOG_TAG, __VA_ARGS__)
//#define LOGW(...) __android_log_print(ANDROID_LOG_WANNAR, LOG_TAG, __VA_ARGS__)
//#define LOGI(...) __android_log_print(ANDROID_LOG_INFO, LOG_TAG, __VA_ARGS__)


extern "C"{
JNIEXPORT jboolean JNICALL
Java_com_afirez_app_opengles_Triangle_init(
        JNIEnv *env,
        jclass clazz);

JNIEXPORT void JNICALL
Java_com_afirez_app_opengles_Triangle_resize(
        JNIEnv *env,
        jclass clazz,
        jint width,
        jint height);

JNIEXPORT void JNICALL
Java_com_afirez_app_opengles_Triangle_draw(
        JNIEnv *env,
        jclass type);

}

static const char VERTEX_SHADER[] =
        "#version 300 es\n"
                "layout(location = 0) in vec4 vPosition;\n"
                "void main(){\n"
                "gl_Position = vPosition;\n"
                "}\n";

static const char FRAGMENT_SHADER[] =
        "#version 300 es\n"
                "precision mediump float;\n"
                "out vec4 fragColor;\n"
                "void main(){\n"
                "fragColor = vec4(1.0,0.0,0.0,1.0);\n"
                "}\n";

static const GLfloat VERTEX[] = {
        0.0f, 0.5f, 0.0f,
        -0.5f, -0.5f, 0.0f,
        0.5f, -0.5f, 0.0f
};

GLuint program;


JNIEXPORT jboolean JNICALL
Java_com_afirez_app_opengles_Triangle_init(
        JNIEnv *env,
        jclass clazz) {

    program = createProgram(VERTEX_SHADER, FRAGMENT_SHADER);
    if (!program) {
        LOGE("程序创建失败");
        return JNI_FALSE;
    }
    glClearColor(0, 0, 0, 0);
    return JNI_TRUE;
}

JNIEXPORT void JNICALL
Java_com_afirez_app_opengles_Triangle_resize(
        JNIEnv *env,
        jclass clazz,
        jint width,
        jint height) {
    glViewport(0, 0, width, height);
    glClear(GL_COLOR_BUFFER_BIT);
}


JNIEXPORT void JNICALL
Java_com_afirez_app_opengles_Triangle_draw(
        JNIEnv *env,
        jclass type) {
    glClear(GL_COLOR_BUFFER_BIT);
    glUseProgram(program);
    glVertexAttribPointer(0, 3, GL_FLOAT, GL_FALSE, 0, VERTEX);
    glEnableVertexAttribArray(0);
    glDrawArrays(GL_TRIANGLES, 0, 3);
}

#endif //OPENGLES_TRIANGLE_H