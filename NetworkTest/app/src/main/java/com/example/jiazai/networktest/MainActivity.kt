package com.example.jiazai.networktest

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_main.*
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.BufferedReader
import java.io.DataOutputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        this.sendRequest = send_request
        this.responseText = response_text

        this.sendRequest.setOnClickListener() {
            //sendRequestWithURL()
            sendRequestWithOkhttp()
        }
    }

    private fun sendRequestWithOkhttp() {
        Thread(Runnable {
            try {
                val client = OkHttpClient()
                val request = Request.Builder().get().url("https://www.baidu.com").build()
                val response = client.newCall(request).execute()
                val responseString = response.body().string()
                showResponse(responseString)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }).start()
    }

    private fun sendRequestWithURL() {
        Thread(Runnable {
            var connection: HttpURLConnection? = null
            var reader: BufferedReader? = null
            try {
                val url = URL("https://www.baidu.com")
                connection = url.openConnection() as HttpURLConnection
                connection.requestMethod = "GET"
                /* if use POST
                *  need to set outputStream of a connection
                *  use & to divide values*/
                //connection.requestMethod = "POST"
                //val outputs = DataOutputStream(connection.outputStream)
                //outputs.writeBytes("username=admin&password=123456")
                connection.connectTimeout = 8000
                connection.readTimeout = 8000
                /* get input stream */
                val inputs = connection.inputStream

                /* read the inputs*/
                reader = BufferedReader(InputStreamReader(inputs))
                val response = StringBuilder()
                var line = reader.readLine()
                if (line  == null) {
                    Log.d("line", "is null")
                }
                while (line != null) {
                    response.append(line)
                    line = reader.readLine()
                }
                showResponse(response.toString())
            } catch (e : Exception) {
                e.printStackTrace()
            } finally {
                try {
                    reader?.close()
                } catch (e: Exception){
                    e.printStackTrace()
                }
                connection?.disconnect()
            }
        }).start()
    }

    /* it is not allowed to change UI in child thread
    *  in this Test, we format response in child thread ( as it is a net work)
    *  use runOnUiThread to run in main process*/
    private fun showResponse(response: String) {
        runOnUiThread {
            this.responseText.setText(response)
        }
    }

    private lateinit var sendRequest:Button
    private lateinit var responseText: TextView
}
