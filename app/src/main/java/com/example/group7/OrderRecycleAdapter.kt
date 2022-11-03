package com.example.group7

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView


class OrderRecycleAdapter (val listOfDocumentId : MutableList<DocumentId>, val restaurantName : String):
    RecyclerView.Adapter<OrderRecycleAdapter.ViewHolder>() {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.order_list_item, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var currentPosition = position

        val currentDocumentId = listOfDocumentId[position]

        holder.currentPosition = position
        currentPosition++
        holder.orderTextView.text = "Order " + currentPosition
        holder.documentId = currentDocumentId.documentId.toString()


    }

    override fun getItemCount(): Int {
        return listOfDocumentId.size
    }


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val orderTextView = itemView.findViewById<TextView>(R.id.orderTextView)
        val seeOrderBtn = itemView.findViewById<Button>(R.id.seeOrderButton)
        var documentId = " "
        var currentPosition = 1

        init {
            seeOrderBtn.setOnClickListener {
                Log.d("!!!", "nuvarande id: $documentId")
//                val intent = Intent(it.context, DetailedOrderActivity::class.java)
//                intent.putExtra("documentId", documentId)
//                intent.putExtra("restaurant", restaurantName)
//                it.context.startActivity(intent)
            }
        }
    }

}