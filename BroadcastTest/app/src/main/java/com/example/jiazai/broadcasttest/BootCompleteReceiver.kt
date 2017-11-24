package com.example.jiazai.broadcasttest

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast

/**
 * Created by jiazai on 17-11-24.
 */
class BootCompleteReceiver:BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        Toast.makeText(context,"Boot Complete", Toast.LENGTH_SHORT).show()
    }
}