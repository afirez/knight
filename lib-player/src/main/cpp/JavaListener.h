//
// Created by afirez on 2018/11/22.
//

#ifndef KNIGHT_JAVALISTENER_H
#define KNIGHT_JAVALISTENER_H


#include <jni.h>

class JavaListener {
public:
    JavaVM *jvm;
    JNIEnv *jniEnv;
    jobject jobj;
    jmethodID jmid;

public:
    JavaListener(JavaVM *jvm, JNIEnv *env, jobject obj);
    ~JavaListener();

    void onError(int threadType, int code, const char *msg);

};


#endif //KNIGHT_JAVALISTENER_H
