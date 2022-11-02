package com.example.group7

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView


class ShoppingCartRecycleAdapter(val context: Context, val shoppingCartList: MutableList<MenuItem?>, val clickListener: listClickListener) : RecyclerView.Adapter<ShoppingCartRecycleAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.shopping_cart_list_item, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.bind(shoppingCartList.get(position)!!)
    }

    override fun getItemCount(): Int {
        return shoppingCartList.size
    }


    inner class ViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        val menuItem = itemView.findViewById<TextView>(R.id.menuItemTextView)
        val addButton = itemView.findViewById<ImageView>(R.id.addImage)
        val removeButton = itemView.findViewById<ImageView>(R.id.removeImage)
        val countTextView = itemView.findViewById<TextView>(R.id.countTextView)
        val layout = itemView.findViewById<ConstraintLayout>(R.id.constraintLayout)
        val addMoreItemButton = itemView.findViewById<TextView>(R.id.addMoreItemButton)
        val itemTotalPriceTextView = itemView.findViewById<TextView>(R.id.itemTotalPriceTextView)

        fun bind(currentItem: MenuItem) {
            menuItem.text = currentItem.name
            countTextView.text = currentItem.totalCart.toString()
            itemTotalPriceTextView.text = getItemTotalPrice(currentItem).toString() + " kr"


            if (currentItem == DataManager.itemInCartList.last()) {
                layout.background = null
                addMoreItemButton.visibility = View.VISIBLE
            }

            addButton.setOnClickListener {
                increaseItemInCart(currentItem)

                val totalItemPrice = getItemTotalPrice(currentItem)
                itemTotalPriceTextView.text = totalItemPrice.toString() + " kr"
                clickListener.updateInCart()

            }
            removeButton.setOnClickListener {
                addButton.setImageResource(R.drawable.add_circle)
                reduceItemInCart(currentItem)
                val totalInCart = getTotalItem(currentItem)
                if (totalInCart < 1) {
                    removeItem(currentItem)
                    notifyDataSetChanged()
                }

                if (DataManager.itemInCartList.isEmpty()) {
                    clickListener.finishActivity()
                } else {
                    clickListener.updateInCart()
                    val totalItemPrice = getItemTotalPrice(currentItem)
                    itemTotalPriceTextView.text = totalItemPrice.toString() + " kr"
                }
            }

            addMoreItemButton.setOnClickListener {
                clickListener.finishActivity()
            }

        }
        fun getItemTotalPrice (currentItem: MenuItem) : Int? {
            return currentItem.price?.times(currentItem.totalCart)
        }

        fun reduceItemInCart (currentItem: MenuItem) {
            val cloneList = mutableListOf<MenuItem>()
            for(item in DataManager.itemInCartList) {
                if (item != null) {
                    cloneList.add(item)
                }
            }
            var total = 0
            for (item in cloneList) {
                if (item.name == currentItem.name) {
                    item.totalCart--
                    total = item.totalCart
                    countTextView.text = total.toString()
                    clickListener.updateInCart()

                    if(item.totalCart < 1) {
                        DataManager.itemInCartList.remove(currentItem)
                        notifyDataSetChanged()
                    }
                }
            }
        }

        fun increaseItemInCart (currentItem: MenuItem) {
            var total = 0
            for (item in DataManager.itemInCartList) {
                if (item != null) {
                    if (item.name == currentItem.name) {
                        if (item.totalCart < 10) {
                            item.totalCart++
                            total = item.totalCart
                            countTextView.text = total.toString()
                            if(item.totalCart == 10) {
                                addButton.setImageResource(R.drawable.light_add_circle)
                            }
                        }
                    }
                }
            }
        }

        fun getTotalItem (currentItem: MenuItem): Int {
            return currentItem.totalCart
        }

        fun removeItem (currentItem: MenuItem) {
            DataManager.itemInCartList.remove(currentItem)
        }

    }
    interface listClickListener{
        fun finishActivity()
        fun updateInCart()
    }

}
