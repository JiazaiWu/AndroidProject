package com.example.jiazai.cameraalbumtest

import android.Manifest
import android.annotation.TargetApi
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v4.content.FileProvider
import android.widget.ImageView
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File
import java.io.FileNotFoundException
import java.io.IOException

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        pictureView = picture
        val takephoto = take_photo
        val chooseFromAlbum = choose_album

        takephoto.setOnClickListener() {
            val outputImage = File(getExternalFilesDir(null), "ouput_" + System.currentTimeMillis().toString()+".jpg")
            try {
                if (outputImage.exists()) {
                    outputImage.delete()
                }
                outputImage.createNewFile()
            } catch (e: IOException) {
                e.printStackTrace()
            }
            if (Build.VERSION.SDK_INT >= 24) {
                imageUri = FileProvider.getUriForFile(this, "com.example.jiazai.cameraalbumtest.fileprovider", outputImage)
            } else {
                imageUri = Uri.fromFile(outputImage)
            }
            val intent = Intent("android.media.action.IMAGE_CAPTURE")
            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri)
            startActivityForResult(intent, TAKE_PHOTO)
        }

        chooseFromAlbum.setOnClickListener() {
            if (ContextCompat.checkSelfPermission(this,Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    .equals(PackageManager.PERMISSION_GRANTED)) {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),1)
            } else {

            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when(requestCode) {
            TAKE_PHOTO-> {
                if (resultCode == Activity.RESULT_OK) {
                    try {
                        val bitmap = BitmapFactory.decodeStream(contentResolver.openInputStream(imageUri))
                        picture.setImageBitmap(bitmap)
                    } catch (e: FileNotFoundException) {
                        e.printStackTrace()
                    }
                }
            }
            CHOOSE_PHOTO-> {
                if (resultCode == Activity.RESULT_OK) {
                    if (Build.VERSION.SDK_INT >= 19) {

                    } else {

                    }
                }
            }
        }
    }

    private fun openAlbum() {
        val intent = Intent("android.intent.action.GET_CONTENT")
        intent.setType("image/*")
        startActivityForResult(intent, CHOOSE_PHOTO)
    }

    @TargetApi(19)
    private fun handleImageOnKitKat(data: Intent) {
        var imagePath:String?
        val uri = data.data
        if (DocumentsContract.isDocumentUri(this, uri)) {
            // handle by document id
            val docid = DocumentsContract.getDocumentId(uri)
            
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when(requestCode) {
            1->{
                if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openAlbum()
                }
            }
            else->{
                Toast.makeText(this, "you denied the permission", Toast.LENGTH_SHORT).show()
            }
        }
    }

    public companion object {
        final val TAKE_PHOTO = 1
        final val CHOOSE_PHOTO = 2
    }
    private lateinit var pictureView:ImageView
    private lateinit var imageUri:Uri
}
