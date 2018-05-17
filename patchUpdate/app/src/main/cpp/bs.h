//
// Created by jiazai on 18-5-17.
//

#ifndef PATCHUPDATE_BS_H
#define PATCHUPDATE_BS_H

#endif //PATCHUPDATE_BS_H

#include <malloc.h>
#include <jni.h>
int mydiff(int argc,char *argv[]);
int mypatch(int argc,char * argv[]);
JNIEXPORT jint JNICALL
Java_com_example_jiazai_patchupdate_MainActivity_patchUpdate
        (JNIEnv *env, jobject instance, jstring oldpath_, jstring newpath_,jstring path_);
JNIEXPORT jint JNICALL
Java_com_example_jiazai_patchupdate_MainActivity_diff
        (JNIEnv *env, jobject instance, jstring oldpath_, jstring newpath_, jstring path_);