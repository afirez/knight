//
// Created by afirez on 24/11/2018.
//

#ifndef KNIGHT_CALLONPREPARED_H
#define KNIGHT_CALLONPREPARED_H


#include <jni.h>
#include "android_log.h"

#define TYPE_MAIN_THREAD 0
#define TYPE_WORKER_THREAD 1


class CallOnPrepared {
public:
    JavaVM *javaVM;
    JNIEnv *jniEnv;
    jobject jobj;
    jmethodID jmid;

public:
    CallOnPrepared(JavaVM *javaVM, JNIEnv *jniEnv, jobject jobj);
    ~CallOnPrepared();


    void onPrepared(int type);
};


#endif //KNIGHT_CALLONPREPARED_H
