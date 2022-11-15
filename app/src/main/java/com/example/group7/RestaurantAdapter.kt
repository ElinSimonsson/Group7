package com.example.group7

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.imageview.ShapeableImageView


class RestaurantAdapter(private val restaurantList: ArrayList<RestaurantsData>) :
    RecyclerView.Adapter<RestaurantAdapter.MyViewHolder>() {

    private lateinit var mListener: onItemClickListener

    interface onItemClickListener {

        fun onItemClick(position: Int)
    }

    fun setOnItemClickListener(listener: onItemClickListener) {

        mListener = listener

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.restaurant_list_item,
            parent, false
        )
        return MyViewHolder(itemView, mListener)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        val currentItem = restaurantList[position]
        val context = holder.itemView.context
        holder.titleImage.setImageResource(currentItem.titleImage)
        holder.restaurantHeading.text = context.getString(R.string.mainRestaurantHeading_textview, currentItem.restaurantHeading)
        holder.distanceTextView.text = context.getString(R.string.mainAddress_textview, currentItem.addressTextView)
        holder.restaurantName = currentItem.restaurantHeading
    }


    override fun getItemCount(): Int {
        return restaurantList.size
    }

    class MyViewHolder(itemView: View, listener: onItemClickListener) :
        RecyclerView.ViewHolder(itemView) {


        val titleImage: ShapeableImageView = itemView.findViewById(R.id.title_image)
        val restaurantHeading: TextView = itemView.findViewById(R.id.restaurantHeading)
        val distanceTextView: TextView = itemView.findViewById(R.id.addressTV)
        var restaurantName = ""

        init {
            itemView.setOnClickListener {

                listener.onItemClick(adapterPosition)

            }
        }
    }
}