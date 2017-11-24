package com.example.jiazai.broadcasttest

import android.Manifest
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val intentFilter = IntentFilter()
        intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE")
        networkChangeReceiver = NetworkChangeReceiver()
        registerReceiver(networkChangeReceiver, intentFilter)

        val button = button
        button.setOnClickListener() {
            val intent = Intent("com.example.jiazai.broadcasttest.MY_BROADCAST")
            sendBroadcast(intent)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(networkChangeReceiver)
    }

    inner class NetworkChangeReceiver: BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            // check permission
            if (ContextCompat.checkSelfPermission(this@MainActivity, Manifest.permission.ACCESS_NETWORK_STATE)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this@MainActivity, arrayOf(Manifest.permission.ACCESS_NETWORK_STATE), 1)
            }

            val connectionManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val networkInfo:NetworkInfo? = connectionManager.activeNetworkInfo
            if (networkInfo != null && networkInfo.isAvailable) {
                Toast.makeText(context, "network is avaliable", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(context, "network is unavailable", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private var networkChangeReceiver: NetworkChangeReceiver? = null
}
