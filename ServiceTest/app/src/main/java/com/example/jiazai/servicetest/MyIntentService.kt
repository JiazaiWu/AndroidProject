package com.example.jiazai.servicetest

import android.app.IntentService
import android.content.Intent
import android.util.Log

/**
 * Created by User on 2017/12/28.
 */
class MyIntentService: IntentService("MyIntentService") {
    override fun onHandleIntent(intent: Intent?) {
        Log.d("MyIntentService", "Thread is "+Thread.currentThread().id)
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("MyIntentService", "onDestroy")
    }
}