package com.example.jiazai.aidltest

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.IBinder
import android.util.Log

class MainActivity : AppCompatActivity() {

    private var carService: ICar? = null
    private val connection = object: ServiceConnection {
        override fun onServiceConnected(p0: ComponentName?, p1: IBinder?) {
            carService = ICar.Stub.asInterface(p1)
            carService?.startRun()
            Log.d("Main", "Car should run")
        }

        override fun onServiceDisconnected(p0: ComponentName?) {
            carService = null
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val intent = Intent(this, CarService::class.java)
        startService(intent)
        bindService(intent, connection, Context.BIND_AUTO_CREATE)
        Log.d("Main", "bind done")
    }

    override fun onDestroy() {
        carService?.stopRun()
        Log.d("Main", "Car should stop")
        unbindService(connection)
        super.onDestroy()
    }
}
