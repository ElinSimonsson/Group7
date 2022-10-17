package com.example.group7

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class MenuRecycleAdapter (val meals : ArrayList<Meal>) :
        RecyclerView.Adapter<MenuRecycleAdapter.ViewHolder>() {

   // val layoutInflater = LayoutInflater.from(context)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.menu_list_item, parent, false)
           // layoutInflater.inflate(R.layout.menu_list_item, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        //val meal = meals[position]

        holder.nameView.text = meals[position].name
        holder.priceView.text = meals [position].price
    }

    override fun getItemCount(): Int = meals.size




    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameView = itemView.findViewById<TextView>(R.id.nameTextView)
        val priceView = itemView.findViewById<TextView>(R.id.priceTextView)
    }
}
