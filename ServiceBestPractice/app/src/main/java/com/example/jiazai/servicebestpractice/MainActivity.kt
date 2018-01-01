package com.example.jiazai.servicebestpractice

import android.Manifest
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.content.pm.PackageManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.IBinder
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity(), View.OnClickListener{

    private var downloadBinder: DownloadService.DownloadBinder? = null
    private var connection = object:ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            downloadBinder = service as DownloadService.DownloadBinder
        }

        override fun onServiceDisconnected(p0: ComponentName?) {

        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val startDownload = start_download
        val pauseDownload = pause_download
        val cancelDownload = cancel_download
        startDownload.setOnClickListener(this)
        pauseDownload.setOnClickListener(this)
        cancelDownload.setOnClickListener(this)
        val intent = Intent(this, DownloadService::class.java)
        startService(intent)
        bindService(intent, connection, Context.BIND_AUTO_CREATE)
        if (!ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .equals(PackageManager.PERMISSION_GRANTED)) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), 1)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when(requestCode) {
            1->{
                if(grantResults.isNotEmpty() && grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "you deny the permission and app can not run install", Toast.LENGTH_SHORT).show()
                    finish()
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        unbindService(connection)
    }

    override fun onClick(v: View) {
        when(v.id) {
            R.id.start_download->{
                val uri = "https://raw.githubusercontent.com/guolindev/eclipse/master/eclipse-inst-win64.exe"
                downloadBinder?.startDownload(uri)
            }
            R.id.pause_download->{
                downloadBinder?.pauseDownload()
            }
            R.id.cancel_download->{
                downloadBinder?.cancelDownload()
            }
        }
    }
}
