package com.example.group7

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners

class AdminDrinkAdapter(val adminDrinkMenu : MutableList<AdminMenuItem>,val restaurantName : String,val type : String) : RecyclerView.Adapter<AdminDrinkAdapter.ViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AdminDrinkAdapter.ViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.admin_food_list_item, parent, false)
        return ViewHolder(itemView)
    }


    override fun getItemCount(): Int {
        return adminDrinkMenu.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val currentItem = adminDrinkMenu[position]

        holder.nameView.text = currentItem.name
        holder.priceView.text = "${currentItem.price} kr"
        var imageURL = currentItem.imageURL
        holder.id = currentItem.documentId.toString()


        val radius = 30
        Glide.with(holder.menuImage)
            .load(imageURL)
            .error(R.drawable.ic_launcher_background)
            .centerCrop()
            .transform(RoundedCorners(radius))
            .into(holder.menuImage)

    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameView: TextView = itemView.findViewById<TextView>(R.id.adminDrinkNameText)
        val priceView: TextView = itemView.findViewById<TextView>(R.id.adminDrinkPriceText)
        var menuImage: ImageView = itemView.findViewById<ImageView>(R.id.adminDrinkItemImage)
        var id = ""


        init {
            itemView.setOnClickListener{

                val adminAdapterIntent = Intent(it.context, AdminDisplayItem_Activity::class.java)
                adminAdapterIntent.putExtra(DOCUMENT_ID,id)
                adminAdapterIntent.putExtra(RES_NAME_ADAPTER,restaurantName)
                adminAdapterIntent.putExtra(TYPE, type)
                it.context.startActivity(adminAdapterIntent)
            }
        }
    }

}