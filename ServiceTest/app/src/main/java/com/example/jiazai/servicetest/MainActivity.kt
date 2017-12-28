package com.example.jiazai.servicetest

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), View.OnClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d("MyService", "activity onCreate")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val startService = start_service
        val stopService = stop_service
        val startIntentService = start_intetn_service
        val stopIntentService = stop_intent_service
        startService.setOnClickListener(this)
        stopService.setOnClickListener(this)
        startIntentService.setOnClickListener(this)
        stopIntentService.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        when(v.id) {
            R.id.start_service->{
                val intent = Intent(this, MyService::class.java)
                startService(intent)
            }
            R.id.stop_service->{
                val intent = Intent(this, MyService::class.java)
                stopService(intent)
            }
            R.id.start_intetn_service->{
                val intent = Intent(this, MyIntentService::class.java)
                startService(intent)
            }
            R.id.stop_intent_service->{
                val intent = Intent(this, MyIntentService::class.java)
                stopService(intent)
            }
        }
    }
}
