package com.example.jiazai.bestfragpractice

import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

/**
 * Created by jiazai on 17-11-17.
 * This fragment is to show the one piece of news
 * newsTitleText content the title
 * mewsContentText content the text
 */
class NewsFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater?.inflate(R.layout.news_content_frag, container, false)
        Log.d(TAG,"get here")
        newsTitleText = view.findViewById<TextView>(R.id.news_title) as TextView
        newsContentText = view.findViewById<TextView>(R.id.news_content) as TextView
        Log.d(TAG, "get here222")
        return view
    }
    public fun refresh(newsTitle: String,  newsContent: String) {
        Log.d(TAG, "refresh")
        newsTitleText.setText(newsTitle)
        newsContentText.setText(newsContent)
    }

    private lateinit var newsTitleText:TextView
    private lateinit var newsContentText:TextView
    private val TAG: String = "NewsFragment"


}