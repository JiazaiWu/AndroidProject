package com.example.jiazai.recyclerviewtest

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.example.jiazai.listviewtest.Fruit

/**
 * Created by jiazai on 17-11-21.
 */
class FruitAdapter: RecyclerView.Adapter<FruitAdapter.ViewHoler> {

    constructor(fruitList: List<Fruit>) {
        this.mFruitList = fruitList
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHoler{
        val view = LayoutInflater.from(parent?.context).inflate(R.layout.fruit_item, parent, false)
        val holder = ViewHoler(view)
        return holder
    }

    override fun onBindViewHolder(holder: ViewHoler?, position: Int) {
        val fruit = mFruitList.get(position)
        holder?.fruitImage?.setImageResource(fruit.imageId)
        holder?.fruitName?.setText(fruit.name)
    }

    override fun getItemCount(): Int {
        return mFruitList.size
    }

    inner class ViewHoler: RecyclerView.ViewHolder {
        val fruitImage: ImageView
        val fruitName: TextView

        constructor(view: View):super(view) {
            this.fruitImage = view.findViewById(R.id.fruit_image)
            this.fruitName = view.findViewById(R.id.fruit_name)
        }
    }

    private var mFruitList: List<Fruit>
}