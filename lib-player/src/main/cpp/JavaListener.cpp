//
// Created by afirez on 2018/11/22.
//

#include "JavaListener.h"

JavaListener::JavaListener(JavaVM *vm, JNIEnv *env, jobject obj) {
    jvm = vm;
    jniEnv = env;
    jobj = obj;
    jclass clz  = env->GetObjectClass(jobj);
    if(!clz) {
        return;
    }
    jmid = env->GetMethodID(clz, "onError", "(ILjava/lang/String;)V");
}

JavaListener::~JavaListener() {
//    jniEnv->DeleteGlobalRef(jobj);
}

void JavaListener::onError(int threadType, int code, const char *msg) {
    if (threadType == 0) { //worker thread
        JNIEnv *env;
        jvm->AttachCurrentThread(&env, 0);

        jstring jmsg = env->NewStringUTF(msg);
        env->CallVoidMethod(jobj, jmid, code, jmsg);
        env->DeleteLocalRef(jmsg);

        jvm->DetachCurrentThread();
    } else if(threadType == 1) { // main thread
        jstring jmsg = jniEnv->NewStringUTF(msg);
        jniEnv->CallVoidMethod(jobj, jmid, code, jmsg);
        jniEnv->DeleteLocalRef(jmsg);
    }
}
