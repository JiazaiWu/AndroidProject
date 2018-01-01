package com.example.jiazai.servicebestpractice

import android.os.AsyncTask
import android.os.Environment
import android.util.Log
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.File
import java.io.IOException
import java.io.InputStream
import java.io.RandomAccessFile

/**
 * Created by User on 2017/12/30.
 */
class DownloadTask:  AsyncTask<String, Int, Int> {

    companion object {
        val TYPE_SUCCESS = 0
        val TYPE_FAILED = 1
        val TYPE_PAUSEED = 2
        val TYPE_CANCELED = 3
    }

    private var listener:DownloadLister
    private var isCanceled = false
    private var isPaused = false
    private var lastProgress = 0

    constructor(listener: DownloadLister) {
        this.listener = listener
    }

    override fun doInBackground(vararg params: String?): Int {
        var inputStream :InputStream? = null
        /* RandomAccessFile 类似c的fd，可以seek*/
        var savedFile : RandomAccessFile? = null
        var file:File? = null
        try {
            var downloadedLength:Long = 0
            val downloadUri = params[0]
            Log.d("DownloadTask", "uri "+downloadUri)
            val filename = downloadUri?.substring(downloadUri.lastIndexOf("/"))
            val savePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).path
            val file = File(savePath+filename)
            /* 已经下载的大小*/
            if (file.exists()) {
                downloadedLength = file.length()
            }
            val contentLength = getContentLength(downloadUri)
            if (contentLength == 0L) {
                return TYPE_FAILED
            } else if (contentLength == downloadedLength) {
                return TYPE_SUCCESS
            }
            val client = OkHttpClient()
            val request = Request.Builder().addHeader("RANGE", "bytes="+downloadedLength+"-")
                    .url(downloadUri).build()
            Log.d("DownloadTask", "get response")
            val response = client.newCall(request).execute()
            if (response != null) {
                inputStream = response.body()!!.byteStream()
                savedFile = RandomAccessFile(file, "rw")
                savedFile.seek(downloadedLength)
                var b = ByteArray(1024)
                var total = 0
                var len = inputStream.read(b)
                Log.d("DownloadTask", "readed "+len)
                while (len != -1) {
                    if (isCanceled) {
                        return TYPE_CANCELED
                    } else if (isPaused) {
                        return TYPE_PAUSEED
                    } else {
                        total += len
                        savedFile.write(b, 0, len)
                        val progress:Int = ((total + downloadedLength) * 100 / contentLength).toInt()
                        publishProgress(progress)
                    }
                    len = inputStream.read(b)
                    Log.d("DownloadTask", "readed "+len)
                }
                response.close()
                return TYPE_SUCCESS
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            try {
                inputStream?.close()
                savedFile?.close()
                if (isCanceled) {
                    file?.delete()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        return TYPE_FAILED
    }

    override fun onProgressUpdate(vararg values: Int?) {
        val progress:Int = values[0]!!
        if (progress > lastProgress) {
            listener.onProgress(progress)
            lastProgress = progress
        }
    }

    override fun onPostExecute(result: Int?) {
        when (result) {
            TYPE_FAILED -> listener.onFail()
            TYPE_SUCCESS -> listener.onSuccess()
            TYPE_CANCELED -> listener.onCancel()
            TYPE_PAUSEED -> listener.onPause()
        }
    }

    public fun pauseDownload() {
        isPaused = true
    }

    public fun cancelDownload() {
        isCanceled = true
    }

    @Throws(IOException::class)
    private fun getContentLength(fileUri: String?):Long{
        val client = OkHttpClient()
        val request = Request.Builder().url(fileUri).build()
        val response = client.newCall(request).execute()
        if (response != null && response.isSuccessful) {
            val contentLength = response.body()!!.contentLength()
            response.close()
            return contentLength
        }
        return 0L
    }
}