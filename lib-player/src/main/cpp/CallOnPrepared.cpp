//
// Created by afirez on 24/11/2018.
//

#include "CallOnPrepared.h"


CallOnPrepared::CallOnPrepared(JavaVM *javaVM, JNIEnv *jniEnv, jobject jobj) {
    this->javaVM = javaVM;
    this->jniEnv = jniEnv;
    this->jobj = jniEnv->NewGlobalRef(jobj);

    jclass jclz = jniEnv->GetObjectClass(jobj);

    if(!jclz) {
        if(LOG_DEBUG) {
            LOGW("Error on get jclass");
        }
    }

    this->jmid = jniEnv->GetMethodID(jclz, "onPrepared", "()V");
}




void CallOnPrepared::onPrepared(int type) {
    if (type == TYPE_MAIN_THREAD) {
        this->jniEnv->CallVoidMethod(this->jobj, this->jmid);
    } else if (type == TYPE_WORKER_THREAD) {
        JNIEnv *env;

        if(this->javaVM->AttachCurrentThread(&env, 0) != JNI_OK) {
            if(LOG_DEBUG) {
                LOGW("Error on get JNIEnv");
                return;
            }
        }

        env->CallVoidMethod(this->jobj, this->jmid);

        this->javaVM->DetachCurrentThread();
    }

}
