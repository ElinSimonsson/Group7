package com.example.group7

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class MenuAdapter(val context : Context, val menu : MutableList<MenuItem>) : RecyclerView.Adapter<MenuAdapter.ViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.list_menu_item,parent,false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = menu[position]

        holder.nameView.text = currentItem.name
        holder.priceView.text = currentItem.price.toString()

        Glide.with(context)
            .load(currentItem.imageURL)
            .into(holder.menuImage)

    }

    override fun getItemCount(): Int {
       return menu.size
    }
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val nameView = itemView.findViewById<TextView>(R.id.nameTextView)
        val priceView = itemView.findViewById<TextView>(R.id.priceTextView)
        var menuImage: ImageView = itemView.findViewById(R.id.menuItemImageView)


        init {
            itemView.setOnClickListener{

            }
        }
    }
}