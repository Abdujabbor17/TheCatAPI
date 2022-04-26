package com.masterandroid.thecatapi_app.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.masterandroid.thecatapi_app.R
import com.masterandroid.thecatapi_app.model.ResponseItem

class HomeAdapter(private val context: Context, var items:ArrayList<ResponseItem>, private val click: (ResponseItem) -> Unit):
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    lateinit var iv_delete:ImageView

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_cats,parent,false)
        return HomeViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = items[position]

        if (holder is HomeViewHolder){
            val iv_image = holder.iv_image

            Glide.with(holder.itemView.context)
                .load(item.url)
                .error(R.drawable.sad_duck)
                .placeholder(R.drawable.img)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(iv_image)

            Log.d("URL",item.url)
            holder.linearClick.setOnLongClickListener {
                click(item)
                true
            }
        }
    }
    override fun getItemCount(): Int {
        return items.size
    }

    private var onItemClickListener: ((ResponseItem) -> Unit)? = null



     class HomeViewHolder(view:View):RecyclerView.ViewHolder(view){
        var iv_image:ImageView = view.findViewById(R.id.iv_image)
         val linearClick: LinearLayout = view.findViewById(R.id.linearClick)
    }
}
