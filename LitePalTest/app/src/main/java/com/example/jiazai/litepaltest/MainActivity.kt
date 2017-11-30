package com.example.jiazai.litepaltest

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import kotlinx.android.synthetic.main.activity_main.*
import org.litepal.tablemanager.Connector

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val Button = create_database
        Button.setOnClickListener() {
            Connector.getDatabase()
        }
    }
}
