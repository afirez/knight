//
// Created by afirez on 21/11/2018.
//

#include <jni.h>

#include <pthread.h>
#include <unistd.h>

#include "android_log.h"

extern "C" {
#include <libavformat/avformat.h>
}

#include "queue"
#include "JavaListener.h"

std::queue<int> queue;
pthread_t p_producor;
pthread_t p_customer;
pthread_mutex_t mutex;
pthread_cond_t cond;

JavaVM *jvm;
JavaListener *javaListener;
pthread_t worker;

void *work_callback(void *data) {
    JavaListener *javaListener = (JavaListener *)(data);
    javaListener->onError(0, 101, "C++ call Java from worker thread");
    pthread_exit(&worker);
}


void *produce_callback(void *data) {
    while (1) {
        pthread_mutex_lock(&mutex);
        queue.push(1);
        LOGI("produce， size: %d", queue.size());
        pthread_cond_signal(&cond);
        pthread_mutex_unlock(&mutex);
        sleep(3);
    }
    pthread_exit(&p_producor);
}

void *custom_callback(void *data) {
    while (1) {
        pthread_mutex_lock(&mutex);
        if (queue.size() <= 0) {
            LOGI("customer wait...");
            pthread_cond_wait(&cond, &mutex);
        } else {
            queue.pop();
            LOGI("custom， size: %d", queue.size());
        }
        pthread_mutex_unlock(&mutex);
        usleep(1000 * 500);
    }
    pthread_exit(&p_customer);
}

extern "C"
JNIEXPORT jstring JNICALL
Java_com_afirez_lib_player_One_helloWorld(JNIEnv *env, jobject jobj) {
    av_register_all();
    AVCodec *p_codec = av_codec_next(NULL);

    while (p_codec != NULL) {
        switch (p_codec->type) {
            case AVMEDIA_TYPE_VIDEO:
                LOGI("[Video]: %s", p_codec->name);
                break;
            case AVMEDIA_TYPE_AUDIO:
                LOGI("[Audio]: %s", p_codec->name);
                break;
            default:
                LOGI("[Other]: %s", p_codec->name);
                break;
        }
        p_codec = p_codec->next;

    }

    return env->NewStringUTF("Hello World");
}

extern "C"
JNIEXPORT jstring JNICALL
Java_com_afirez_lib_player_One_producerCustomer(JNIEnv *env, jobject jobj) {

    for (int i = 0; i < 10; ++i) {
        queue.push(i);
    }

    pthread_mutex_init(&mutex, NULL);
    pthread_cond_init(&cond, NULL);

    pthread_create(&p_producor, NULL, produce_callback, NULL);
    pthread_create(&p_customer, NULL, custom_callback, NULL);

    return env->NewStringUTF("Producer Customer");
}

extern "C"
JNIEXPORT jstring JNICALL
Java_com_afirez_lib_player_One_callbackFromC(JNIEnv *env, jobject jobj) {

    javaListener = new JavaListener(jvm, env, env->NewGlobalRef(jobj));

    // main thread
    javaListener->onError(1, 100, "C++ call Java from main thread");

    //worker thread
    pthread_create(&worker, NULL, work_callback, javaListener);

    return env->NewStringUTF("Callback From C");
}

//JNIEXPORT jint JNICALL JNI_OnLoad(JavaVM *vm, void *reserved) {
//    JNIEnv *env;
//    jvm = vm;
//
//    if(vm->GetEnv((void **)(&env), JNI_VERSION_1_6) != JNI_OK) {
//        return -1;
//    }
//
//    return JNI_VERSION_1_6;
//}

