//
// Created by jiazai on 18-5-17.
//
#include "bs.h"
JNIEXPORT jint JNICALL
Java_com_example_jiazai_patchupdate_MainActivity_patchUpdate
        (JNIEnv *env, jobject instance, jstring oldpath_, jstring newpath_,jstring path_) {
    const char* argv[4];
    argv[0] = "bspatch";
    argv[1] = (*env)->GetStringUTFChars(env,oldpath_, 0);
    argv[2] = (*env)->GetStringUTFChars(env,newpath_, 0);
    argv[3] = (*env)->GetStringUTFChars(env, path_, 0);
    //该函数用于合并差分包
    mypatch(4,argv);
    (*env)->ReleaseStringUTFChars(env,oldpath_, argv[1]);
    (*env)->ReleaseStringUTFChars(env,newpath_, argv[2]);
    (*env)->ReleaseStringUTFChars(env,path_,argv[3]);
    return 0;
}

JNIEXPORT jint JNICALL
Java_com_example_jiazai_patchupdate_MainActivity_diff
        (JNIEnv *env, jobject instance, jstring oldpath_, jstring newpath_, jstring path_) {
    const char* argv[4];
    argv[0] = "bsdiff";
    argv[1] = (*env)->GetStringUTFChars(env,oldpath_, 0);
    argv[2] = (*env)->GetStringUTFChars(env,newpath_, 0);
    argv[3] = (*env)->GetStringUTFChars(env, path_, 0);
    //该函数用于生成差分包
    mydiff(4,argv);
    (*env)->ReleaseStringUTFChars(env,oldpath_, argv[1]);
    (*env)->ReleaseStringUTFChars(env,newpath_, argv[2]);
    (*env)->ReleaseStringUTFChars(env,path_,argv[3]);
    return 0;
}