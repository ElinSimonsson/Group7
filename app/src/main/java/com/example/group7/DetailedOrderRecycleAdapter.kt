package com.example.group7

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class DetailedOrderRecycleAdapter(var listOfOrderItem: MutableList<OrderData>) :
RecyclerView.Adapter<DetailedOrderRecycleAdapter.ViewHolder>(){


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.detailed_order_list_item, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val currentItem = listOfOrderItem[position]
        holder.orderItemTextView.text = currentItem.name
        holder.amountTextView.text = currentItem.amount.toString()


    }

    override fun getItemCount(): Int {
        return listOfOrderItem.size
    }


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val orderItemTextView = itemView.findViewById<TextView>(R.id.orderItemTextView)
        val amountTextView = itemView.findViewById<TextView>(R.id.amountTV)

    }


}