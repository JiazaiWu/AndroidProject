package com.example.jiazai.servicebestpractice

/**
 * Created by User on 2017/12/30.
 */
open interface DownloadLister {
    fun onProgress(progress: Int)
    fun onSuccess()
    fun onFail()
    fun onPause()
    fun onCancel()
}