package com.example.jiazai.databasetest

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.widget.Toast

/**
 * Created by jiazai on 17-11-29.
 */
class MyDatabaseHelper: SQLiteOpenHelper {

    constructor(context: Context, name: String, factory:SQLiteDatabase.CursorFactory?, version: Int):
            super(context, name, factory,version) {
        this.mContext = context
    }
    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(this.CREATE_BOOK)
        db?.execSQL(this.CREATE_CATEGORY)
        Toast.makeText(this.mContext, "create succeed", Toast.LENGTH_SHORT).show()
    }

    override fun onUpgrade(db: SQLiteDatabase?, p1: Int, p2: Int) {
        db?.execSQL("drop table if exists Book")
        db?.execSQL("drop table if exists Category")
        onCreate(db)

    }

    private lateinit var mContext: Context
    private val CREATE_BOOK = "create table book (" +
            "id integer primary key autoincrement," +
            "author text," +
            "price real," +
            "pages integer," +
            "name text)"

    private val CREATE_CATEGORY = "create table Category (" +
            "id integer primary key autoincrement," +
            "category_name text," +
            "category_code integer)"
}