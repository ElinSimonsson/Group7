package com.example.group7

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class DetailedOrderRecycleAdapter(var listOfOrderItem: MutableList<OrderData>) :
    RecyclerView.Adapter<DetailedOrderRecycleAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.detailed_order_list_item, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.bind(listOfOrderItem[position])


    }

    override fun getItemCount(): Int {
        return listOfOrderItem.size
    }


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private var orderItemTextView = itemView.findViewById<TextView>(R.id.orderItemTextView)
        private val amountTextView = itemView.findViewById<TextView>(R.id.amountTV)
        private val context = itemView.context


        fun bind (currentItem : OrderData) {
            orderItemTextView.text = context.getString(R.string.orderItem_textview, currentItem.name)
            amountTextView.text = context.getString(R.string.amount_textview, currentItem.amount)

        }
    }


}