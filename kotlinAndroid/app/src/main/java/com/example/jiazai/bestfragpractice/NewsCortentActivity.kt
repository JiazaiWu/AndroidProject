package com.example.jiazai.bestfragpractice

import android.content.Context
import android.content.Intent
import android.nfc.Tag
import android.os.Bundle
import android.os.PersistableBundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.util.AttributeSet
import android.util.Log
import android.view.View
import kotlinx.android.synthetic.main.news_content.*

/**
 * Created by User on 2017/11/17.
 */

class NewsCortentActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.news_content)
        newsTitle = intent.getStringExtra("news_title")
        newsContent = intent.getStringExtra("news_content")
        Log.d(TAG, "onCreate")
        var newsContentFragment = news_content_fragment_phone as NewsFragment
        newsContentFragment.refresh(newsTitle, newsContent)
    }

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
    }

    companion object {
        public fun acionStart(context: Context?, newsTitle: String, newsContent: String) {
            Log.d("NewsCortentActivity",newsTitle+newsContent)
            val intent = Intent(context, NewsCortentActivity::class.java)
            intent.putExtra("news_title", newsTitle)
            intent.putExtra("news_content", newsContent)
            context!!.startActivity(intent)
        }
    }
    private final val TAG:String = "NewsCortentActivity"
    private var newsTitle: String = ""
    private var newsContent: String = ""
}
