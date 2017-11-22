package com.example.jiazai.bestfragpractice

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.news_title_frag.*

/**
 * Created by User on 2017/11/17.
 */
class NewsTitleFragment : Fragment(){
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        Log.d(TAG, "onCreateView")
        return inflater.inflate(R.layout.news_title_frag, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val newsTitleRecyclerView = news_title_recycler_view
        val layoutmanager = LinearLayoutManager(activity);
        newsTitleRecyclerView.layoutManager = layoutmanager
        val adapter = NewsAdapter(getNews())
        newsTitleRecyclerView.adapter = adapter
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        Log.d(TAG, "onActivityCreated")
        super.onActivityCreated(savedInstanceState)
        if(news_content_layout != null) {
            Log.d(TAG, "is Two Pane")
            this.isTwoPane = true
        } else {
            Log.d(TAG, "not Two Pane")
            this.isTwoPane = false
        }
    }

    inner class NewsAdapter(newsList: List<News>) : RecyclerView.Adapter<NewsAdapter.ViewHolder>() {

        inner class ViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView) {
            var newsTitle:TextView?

            init {
                this.newsTitle = itemView!!.findViewById<View>(R.id.news_title) as TextView
            }
        }

        private var mNewsList: List<News>?

        init {
            this.mNewsList = newsList
        }

        override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent!!.context).inflate(R.layout.news_item, parent, false)
            val holder = ViewHolder(view)

            view.setOnClickListener {
                val news = mNewsList?.get(holder.adapterPosition)
                if (isTwoPane) {
                    val newsContentFragment = news_content_fragment as NewsFragment
                    newsContentFragment.refresh(news!!.titleText, news.contentText)
                } else {
                    Log.d(TAG, "android phone will get here")
                    NewsCortentActivity.acionStart(activity, news!!.titleText, news.contentText)
                }
            }
            return  holder
        }

        override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
            val news = mNewsList!!.get(position)
            holder!!.newsTitle!!.setText(news.titleText)
        }

        override fun getItemCount(): Int {
            return mNewsList!!.size
        }
    }
    private fun getNews():List<News> {
        val newsList = ArrayList<News>()
        for (i in 1..50) {
            val news = News("This is title", "This is content")
            newsList.add(news)
        }
        return  newsList
    }

    private var isTwoPane: Boolean = true
    private var TAG: String = "NewsTitleFragment"
}