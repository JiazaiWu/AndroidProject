package com.example.jiazai.notificationtest

import android.annotation.TargetApi
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.graphics.BitmapFactory
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.NotificationCompat
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        this.sendNotice = send_notice
        this.sendNotice.setOnClickListener() {
            view: View -> when(view.id) {
                R.id.send_notice-> {
                    sendNotification()
                }
                else->{Toast.makeText(this, "nothing to do", Toast.LENGTH_SHORT).show()}
            }
        }
    }

    @TargetApi(26)
    fun sendNotification() {
        val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= 26) {
            Log.d("test", "sdk 26")
            val mChannel = NotificationChannel("channel_01", "wu", NotificationManager.IMPORTANCE_LOW)
            manager.createNotificationChannel(mChannel)
        }
        var notificationBuilder: NotificationCompat.Builder
        notificationBuilder = NotificationCompat.Builder(this, "channel_01")
                .setContentTitle("This is content title")
                .setContentText("This is content text")
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.mipmap.ic_launcher).setLargeIcon(BitmapFactory.decodeResource(resources, R.mipmap.ic_launcher)) 

        val notification = notificationBuilder.build()
        manager.notify(1, notification)
    }

    private lateinit var sendNotice:Button
}
