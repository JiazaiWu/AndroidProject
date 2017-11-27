package com.example.jiazai.bestbroadcastpractice

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        forceOffline = force_offline
        forceOffline.setOnClickListener {
            val intent = Intent("com.example.jiazai.bestbroadcastpractice.FORCE_OFFLINE")
            sendBroadcast(intent)
        }
    }

    private lateinit var forceOffline:Button
}
