package com.example.group7

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners

class AdminMenuAdapter(private val context : Context, val adminMenu : MutableList<AdminMenuItem>,val restaurantName : String) : RecyclerView.Adapter<AdminMenuAdapter.ViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.admin_menu_list_item,parent,false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val currentItem = adminMenu[position]

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
        return adminMenu.size
    }
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameView: TextView = itemView.findViewById<TextView>(R.id.adminNameText)
        val priceView: TextView = itemView.findViewById<TextView>(R.id.adminPriceText)
        var menuImage: ImageView = itemView.findViewById<ImageView>(R.id.adminMenuItemImage)



        init {
            itemView.setOnClickListener{
                val adminAdapterIntent = Intent(context, AdminDisplayItem_Activity::class.java)
                adminAdapterIntent.putExtra(ITEM_POSITION_NAME,nameView.text)
                adminAdapterIntent.putExtra(RESTAURANT_NAME,restaurantName)
                Log.d("!!!","adapter rn : $restaurantName")
                context.startActivity(adminAdapterIntent)
            }
        }
    }
}