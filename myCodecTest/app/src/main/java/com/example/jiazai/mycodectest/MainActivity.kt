package com.example.jiazai.mycodectest

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.media.projection.MediaProjectionManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.util.Log
import android.view.View
import android.widget.Button
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity() : AppCompatActivity(), View.OnClickListener{

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        this.startButtom = start_record
        this.startButtom.setOnClickListener(this)
        this.stopButtom = stop_record
        this.stopButtom.setOnClickListener(this)
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf<String>(Manifest.permission.WRITE_EXTERNAL_STORAGE), 1)
        }
        this.mMediaProjectionManager = getSystemService(Context.MEDIA_PROJECTION_SERVICE) as MediaProjectionManager
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val mediaProjection = mMediaProjectionManager?.getMediaProjection(resultCode, data)
        mScreenRecorder = MediaCodecHelp(mediaProjection)
        Log.d("jiazai","record thead start")
        mScreenRecorder?.start()
    }

    override fun onClick(p0: View?) {
        when(p0?.id) {
            R.id.start_record-> {
                Log.d("jiazai", "to start recording")
                this.mScreenRecorder?.quit()
                this.mScreenRecorder = null
                val captureIntent = mMediaProjectionManager?.createScreenCaptureIntent()
                startActivityForResult(captureIntent, REQUEST_CODE)
            }
            R.id.stop_record-> {
                this.mScreenRecorder?.quit()
                this.mScreenRecorder = null
            }
        }
    }

    private var mMediaProjectionManager: MediaProjectionManager? = null
    private var mScreenRecorder: MediaCodecHelp? = null
    private val REQUEST_CODE = 1
    private lateinit var startButtom: Button
    private lateinit var stopButtom: Button

}
