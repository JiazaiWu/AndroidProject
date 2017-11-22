package com.example.jiazai.recyclerviewtest

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import com.example.jiazai.listviewtest.Fruit
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initFruit()
        val recyclerView = recycler_view
        val layoutManager = LinearLayoutManager(this)
        layoutManager.orientation = LinearLayoutManager.HORIZONTAL
        recyclerView.layoutManager = layoutManager
        val adapter = FruitAdapter(fruitList)
        recyclerView.adapter = adapter
    }

    fun initFruit() {
        val apple = Fruit("apple", R.drawable.apple_pic)
        val banana = Fruit("banana", R.drawable.banana_pic)
        fruitList.add(apple)
        fruitList.add(banana)
    }

    private val fruitList = ArrayList<Fruit>()
}
