package com.example.group7

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.imageview.ShapeableImageView



class RestaurantAdapter(private val restaurantList : ArrayList<RestaurantsData>) :
    RecyclerView.Adapter<RestaurantAdapter.MyViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.restaurant_list_item,
        parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        val currentItem = restaurantList[position]
        holder.titleImage.setImageResource(currentItem.titleImage)
        holder.restaurantHeading.text = currentItem.restaurantHeading
        holder.distanceTextView.text = currentItem.distanceTextView








    }





    override fun getItemCount(): Int {
        return restaurantList.size
    }

    class MyViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){


        val titleImage : ShapeableImageView = itemView.findViewById(R.id.title_image)
        val restaurantHeading : TextView = itemView.findViewById(R.id.restaurantHeading)
        val distanceTextView : TextView = itemView.findViewById(R.id.distanceTextView)





    }

}