package com.example.jiazai.servicebestpractice

import android.annotation.TargetApi
import android.app.*
import android.content.Context
import android.content.Intent
import android.os.Binder
import android.os.Build
import android.os.Environment
import android.os.IBinder
import android.support.v4.app.NotificationCompat
import android.util.Log
import android.widget.Toast
import java.io.File

class DownloadService : Service() {

    override fun onBind(intent: Intent): IBinder? {
        // TODO: Return the communication channel to the service.
        Log.d("DownloadService", "context this "+this)
        Log.d("DownloadService", "context base "+this.baseContext)
        Log.d("DownloadService", "context app "+applicationContext)
        return mBinder
    }

    private val mBinder = DownloadBinder()
    private var downloadTask: DownloadTask? = null
    private var notificationManeger: NotificationManager? = null
    private var downloadUri: String = ""

    /* inner class in kotlin ==> use object */
    private val listener = object:DownloadLister {
        override fun onCancel() {
            downloadTask = null
            stopForeground(true)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                getNotificationManager().notify(1, getNotificationO("Downloading Paused", -1))
            } else {
                getNotificationManager().notify(1, getNotification("Downloading Paused", -1))
            }
        }

        override fun onPause() {
            downloadTask = null
            Toast.makeText(this@DownloadService, "paused", Toast.LENGTH_SHORT)
        }

        override fun onFail() {
            downloadTask = null
            /* remove previous notifaction */
            stopForeground(true)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                getNotificationManager().notify(1, getNotificationO("Downloading Failed", -1))
            } else {
                getNotificationManager().notify(1, getNotification("Downloading Failed", -1))
            }
            Toast.makeText(this@DownloadService, "Download Failed", Toast.LENGTH_SHORT).show()
        }

        override fun onSuccess() {
            downloadTask = null
            stopForeground(true)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                getNotificationManager().notify(1, getNotificationO("Download Success", -1))
            } else {
                getNotificationManager().notify(1, getNotification("Download Success", -1))
            }
            Toast.makeText(this@DownloadService, "Download Success", Toast.LENGTH_SHORT).show()
        }

        override fun onProgress(progress: Int) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                getNotificationManager().notify(1, getNotificationO("Download...", progress))
            } else {
                getNotificationManager().notify(1, getNotification("Download...", progress))
            }
        }
    }

    private fun getNotificationManager() : NotificationManager {
        if (notificationManeger == null)
            notificationManeger = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        return notificationManeger!!
    }

    private fun getNotification(title:String, progress: Int): Notification {
        val intent = Intent(this, MainActivity::class.java)
        val pi = PendingIntent.getActivity(this, 0, intent, 0)
        val builder = NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentIntent(pi)
                .setContentTitle(title)
        if (progress > 0) {
            builder.setContentText(progress.toString()+"%").setProgress(100, progress, false)
        }
        return builder.build()
    }

    @TargetApi(26)
    private fun getNotificationO(title: String, progress: Int): Notification {
        val id = "channel_1"
        val name = "jiazai"
        val noticationChannel = NotificationChannel(id, name, NotificationManager.IMPORTANCE_LOW)
        getNotificationManager().createNotificationChannel(noticationChannel)

        val intent = Intent(this, MainActivity::class.java)
        val pi = PendingIntent.getActivity(this, 0, intent, 0)
        val builder = Notification.Builder(this, id)
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentIntent(pi)
                .setContentTitle(title)
        if (progress > 0) {
            builder.setContentText(progress.toString()+"%").setProgress(100, progress, false)
        }
        return builder.build()
    }

    inner class DownloadBinder:Binder() {
        fun startDownload(uri: String) {
            if (downloadTask == null) {
                downloadUri = uri
                downloadTask = DownloadTask(listener)
                downloadTask!!.execute(downloadUri)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    startForeground(1, getNotificationO("Downloading...", 0))
                } else {
                    startForeground(1, getNotification("Downloading...", 0))
                }
                Toast.makeText(this@DownloadService, "Downloading...", Toast.LENGTH_SHORT).show()
            }
        }

        fun pauseDownload() {
            downloadTask?.pauseDownload()
        }

        fun cancelDownload() {
            if (downloadTask != null) {
                downloadTask!!.cancelDownload()
            } else {
                if (downloadUri != null) {
                    val filename = downloadUri?.substring(downloadUri.lastIndexOf("/"))
                    val savePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).path
                    val file = File(savePath+filename)
                    if (file.exists()) {
                        file.delete()
                    }
                    getNotificationManager().cancel(1)
                    stopForeground(true)
                    Toast.makeText(this@DownloadService, "Canceled", Toast.LENGTH_SHORT).show()
                }
            }
        }

    }
}
