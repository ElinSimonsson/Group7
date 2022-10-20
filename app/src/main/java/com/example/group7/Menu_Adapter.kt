package com.example.group7

import android.content.Context
import android.view.LayoutInflater
import android.view.RoundedCorner
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage

class MenuAdapter(private val context : Context, val menu : MutableList<MenuItem>) : RecyclerView.Adapter<MenuAdapter.ViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.menu_list_item,parent,false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val currentItem = menu[position]

        holder.nameView.text = currentItem.name
        holder.priceView.text = "${currentItem.price} kr"
        var imageURL = currentItem.imageURL

        val radius = 30
        val margin = 10
        Glide.with(context)
            .load(imageURL)
            .error(R.drawable.ic_launcher_background)
            .centerCrop()
            .transform(RoundedCorners(radius))
            .into(holder.menuImage)

    }

    override fun getItemCount(): Int {
       return menu.size
    }
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameView = itemView.findViewById<TextView>(R.id.nameTextView1)
        val priceView = itemView.findViewById<TextView>(R.id.priceTextView1)
        var menuImage = itemView.findViewById<ImageView>(R.id.menuItemImageView)

    }
}