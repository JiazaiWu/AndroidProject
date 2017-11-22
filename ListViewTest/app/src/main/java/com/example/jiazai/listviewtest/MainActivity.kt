package com.example.jiazai.listviewtest

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //use adapter to transfer data to listview
        initFruit()
        //val adapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_2, data)
        val adapter = FruitAdapter(this, R.layout.fruit_item, fruitList)
        val listView = list_view
        listView.adapter = adapter

    }

    private fun initFruit() {
        for (i in 1..50 ) {
            val apple = Fruit("apple", R.drawable.apple_pic)
            val banana = Fruit("banana", R.drawable.banana_pic)
            fruitList.add(apple)
            fruitList.add(banana)
        }
    }

    private val fruitList:ArrayList<Fruit> = ArrayList<Fruit>()
    //private var data: Array<String> = arrayOf("apple", "banana", "orange", "watermelon")
}
