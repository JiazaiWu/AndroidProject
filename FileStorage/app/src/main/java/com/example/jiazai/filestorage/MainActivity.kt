package com.example.jiazai.filestorage

import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import java.io.*
import java.lang.StringBuilder

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        edition = edit
        saveButton = save_data
        saveButton.setOnClickListener() {
            val editor = getSharedPreferences("data", Context.MODE_PRIVATE).edit()
            editor.putString("name", "Tome")
                    .putInt("age", 28)
                    .putBoolean("married", false)
                    .apply()
        }
        restoreButton = restore_data
        restoreButton.setOnClickListener() {
            val pref = getSharedPreferences("data", Context.MODE_PRIVATE)
            val name = pref.getString("name", "")
            val age = pref.getInt("age", 0)
            val married = pref.getBoolean("married", false)
            Log.d(TAG, "name is "+name)
            Log.d(TAG, "age is "+age)
            Log.d(TAG, "married is "+married)
        }

        /*load the data if exist*/
        val storedString = load()
        if (!TextUtils.isEmpty(storedString)) {
            edition.setText(storedString)
            edition.setSelection(storedString.length)
            Toast.makeText(this,"Restoring string", Toast.LENGTH_SHORT).show()
        }



    }

    override fun onDestroy() {
        super.onDestroy()
        val input = edition.text.toString()
        save(input)
    }

    fun load(): String {
        var input:FileInputStream? = null
        var reader:BufferedReader? = null
        val builder = StringBuilder()
        try {
            input = openFileInput("data")
            reader = BufferedReader(InputStreamReader(input))
            var line:String? = reader.readLine()
            if (line != null) {
                builder.append(line)
                line = reader.readLine()
            }
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            try {
                reader?.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        return builder.toString()
    }

    fun save(data: String) {
        var out: FileOutputStream? = null
        var writer: BufferedWriter? = null
        try {
            out = openFileOutput("data", Context.MODE_PRIVATE)
            writer = BufferedWriter(OutputStreamWriter(out))
            writer.write(data)
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            try {
                writer?.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    private lateinit var edition:EditText
    private lateinit var saveButton: Button
    private lateinit var restoreButton:Button
    private val TAG = "MainActivity"
}
