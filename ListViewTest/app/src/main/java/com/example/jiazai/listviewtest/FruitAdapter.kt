package com.example.jiazai.listviewtest

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView

/**
 * Created by jiazai on 17-11-20.
 */
class FruitAdapter : ArrayAdapter<Fruit> {
    constructor(context:Context, textViewResourceId:Int, objects: List<Fruit>) : super(context, textViewResourceId, objects) {
        resourceId = textViewResourceId
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val fruit = getItem(position)
        var view: View // fruit_item.xml
        val viewHolder:ViewHolder
        if (convertView == null) {
            Log.d("wujiazai", "convertView null")
            view = LayoutInflater.from(context).inflate(resourceId, parent, false)
            // A new view, get its image and name, then store to tag
            val fruitImage = view.findViewById<ImageView>(R.id.fruit_image)
            val fruitName = view.findViewById<TextView>(R.id.fruit_name)
            viewHolder = ViewHolder(fruitImage, fruitName)
            view.setTag(viewHolder)
        } else {
            Log.d("wujiazai", "convertView not null")
            view = convertView
            // A Exist view, get image and name from tag
            viewHolder = view.getTag() as ViewHolder
        }
        // do not findview every time getView
        //val fruitImage = view.findViewById<ImageView>(R.id.fruit_image)
        //val fruitName = view.findViewById<TextView>(R.id.fruit_name)
        viewHolder.fruitImage.setImageResource(fruit.imageId)
        viewHolder.fruitName.setText(fruit.name)
        return view
    }

    inner class ViewHolder(val fruitImage:ImageView, val fruitName: TextView)

    private val resourceId: Int
}