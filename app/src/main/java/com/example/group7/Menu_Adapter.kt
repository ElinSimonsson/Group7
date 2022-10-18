package com.example.group7

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.view.menu.MenuView.ItemView
import androidx.recyclerview.widget.RecyclerView

class MenuAdapter(val menu : MutableList<MenuItem>) : RecyclerView.Adapter<MenuAdapter.ViewHolder>(){


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.list_menu_item,parent,false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.nameView.text = menu[position].name
        holder.priceView.text = menu[position].price.toString()

    }

    override fun getItemCount(): Int {
       return menu.size
    }
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val nameView = itemView.findViewById<TextView>(R.id.nameTextView)
        val priceView = itemView.findViewById<TextView>(R.id.priceTextView)


        init {
            itemView.setOnClickListener{

            }
        }
    }
}