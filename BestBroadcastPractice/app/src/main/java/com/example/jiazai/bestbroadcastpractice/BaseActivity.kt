package com.example.jiazai.bestbroadcastpractice

import android.content.*
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.util.Log

/**
 * Created by jiazai on 17-11-24.
 */
open class BaseActivity:AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ActivityCollector.addActivity(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        ActivityCollector.removeActivity(this)
    }

    override fun onResume() {
        super.onResume()
        Log.d("jiazai","Base onResume!!")
        val intentFilter = IntentFilter()
        intentFilter.addAction("com.example.jiazai.bestbroadcastpractice.FORCE_OFFLINE")
        receiver = ForceOfflineReceiver()
        registerReceiver(receiver, intentFilter)

    }

    override fun onPause() {
        super.onPause()
        if (receiver != null) {
            unregisterReceiver(receiver)
            receiver = null
        }
    }
    /**/
    inner class ForceOfflineReceiver: BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent?) {
            val builder = AlertDialog.Builder(context)
            builder.setTitle("Warning")
                    .setMessage("You are forced to be offline. Please try to login aain!")
                    .setCancelable(false)
                    .setPositiveButton("OK") {
                        dialogInterface: DialogInterface?, which: Int ->
                        ActivityCollector.finishAll()
                        val intent = Intent(context, LoginActivity::class.java)
                        context.startActivity(intent)
                    }.show()
        }
    }

    private var receiver: ForceOfflineReceiver? = null
}