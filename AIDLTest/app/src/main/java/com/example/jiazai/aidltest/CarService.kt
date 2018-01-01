package com.example.jiazai.aidltest

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log

class CarService : Service() {

    private val carBinder = object: ICar.Stub() {
        private var running = false

        override fun stopRun(): Boolean {
            if (running) {
                running = false
                Log.d("CarService", "stopped")
            } else {
                Log.d("CarService", "already stopped")
            }
            return true
        }

        override fun startRun(): Boolean {
            if (!running) {
                running = true
                Log.d("CarService", "running")
            } else {
                Log.d("CarService", "already running")
            }
            return true
        }
    }

    override fun onBind(intent: Intent): IBinder? {
        return  carBinder
    }
}
