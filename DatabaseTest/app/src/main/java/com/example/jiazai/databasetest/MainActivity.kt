package com.example.jiazai.databasetest

import android.content.ContentValues
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        dbhelper = MyDatabaseHelper(this, "BookStore", null, 4)
        val button = create_table
        button.setOnClickListener() {
            dbhelper.writableDatabase
        }

        this.addDataButton = add_data
        this.addDataButton.setOnClickListener() {
            Log.d(TAG,"add data")
            val db = dbhelper.writableDatabase
            val value = ContentValues()
            value.put("name", "The Da Vinci Code")
            value.put("author", "Dan Brown")
            value.put("pages", 454)
            value.put("price", 16.96)
            db.insert("Book", null,value)
            value.clear()
        }
        this.queryDataButton = query_data
        this.queryDataButton.setOnClickListener() {
            Log.d(TAG,"query")
            val db = dbhelper.writableDatabase
            val cursor = db.query("Book", null, null, null, null,null, null)
            if (cursor.moveToFirst()) {
                do {
                    val name = cursor.getString(cursor.getColumnIndex("name"))
                    val author = cursor.getString(cursor.getColumnIndex("author"))
                    val pages = cursor.getInt(cursor.getColumnIndex("pages"))
                    val price = cursor.getDouble(cursor.getColumnIndex("price"))

                    Log.d(TAG,"book name is "+name)
                    Log.d(TAG,"book autor is "+author)
                    Log.d(TAG,"book pages is "+pages)
                    Log.d(TAG,"book price is "+price)
                } while (cursor.moveToNext())
            }
            cursor.close()
        }
    }

    private lateinit var dbhelper: MyDatabaseHelper
    private lateinit var addDataButton: Button
    private lateinit var queryDataButton: Button
    private val TAG = "MainActivity"
}
