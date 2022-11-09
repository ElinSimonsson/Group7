package com.example.group7

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners

class FoodRecycleAdapter(var menu: MutableList<MenuItem?>, val clickListener: FoodFragment) :
    RecyclerView.Adapter<FoodRecycleAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {


        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.food_list_item, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(menu.get(position)!!)
    }

    override fun getItemCount(): Int {
        return menu.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameView = itemView.findViewById<TextView>(R.id.nameTextView)
        val priceView = itemView.findViewById<TextView>(R.id.priceTextView)
        var menuImage = itemView.findViewById<ImageView>(R.id.menuItemImageView)
        var countTextView = itemView.findViewById<TextView>(R.id.amountTextView)
        var addImageView = itemView.findViewById<ImageView>(R.id.addImageView)
        var removeImageView = itemView.findViewById<ImageView>(R.id.removeImageView)
        var addToCart = itemView.findViewById<TextView>(R.id.addToCartButton)


        fun bind(currentMenu: MenuItem) {

            nameView.text = currentMenu.name
            priceView.text = "${currentMenu.price} kr"

            checkAlreadyInList(currentMenu)
            for (item in DataManager.itemInCartList) {
                if (currentMenu.name == item?.name) {
                    if (item?.totalCart == 10) {
                        addImageView.setImageResource(R.drawable.light_add_circle)
                    }
                }
            }

            val radius = 30
            Glide.with(menuImage)
                .load(currentMenu.imageURL)
                .error(R.drawable.no_image)
                .centerCrop()
                .transform(RoundedCorners(radius))
                .into(menuImage)

            addToCart.setOnClickListener {
                addToCart.visibility = View.GONE
                addImageView.visibility = View.VISIBLE
                removeImageView.visibility = View.VISIBLE
                countTextView.visibility = View.VISIBLE
                currentMenu.totalCart++
                countTextView.text = currentMenu.totalCart.toString()

                if (currentMenu !in DataManager.itemInCartList) {
                    DataManager.itemInCartList.add(currentMenu)
                    clickListener.addItemToCart(currentMenu)
                }
            }


            removeImageView.setOnClickListener {
                addImageView.setImageResource(R.drawable.add_circle)

                // En klon av DatamManager.itemInCartList skapas
                // för att undvika felkod ConcurrentModificationException

                val cloneList = mutableListOf<MenuItem>()
                for (item in DataManager.itemInCartList) {
                    if (item != null) {
                        cloneList.add(item)
                    }
                }
                var total = 0
                for (item in cloneList) {
                    if (item.name == currentMenu.name) {
                        item.totalCart--
                        total = item.totalCart
                        countTextView.text = total.toString()
                        clickListener.upgradeItemInCart(currentMenu)

                        if (item.totalCart < 1) {
                            DataManager.itemInCartList.remove(currentMenu)
                            addImageView.visibility = View.GONE
                            removeImageView.visibility = View.GONE
                            countTextView.visibility = View.GONE
                            addToCart.visibility = View.VISIBLE
                            clickListener.removeItemFromCart(currentMenu)
                        }
                    }
                }
            }

            addImageView.setOnClickListener {
                var total = 0

                for (item in DataManager.itemInCartList) {
                    if (item != null) {
                        if (item.name == currentMenu.name) {
                            if (item.totalCart < 10) {
                                item.totalCart++
                                total = item.totalCart
                                countTextView.text = total.toString()
                                clickListener.upgradeItemInCart(currentMenu)
                                if (item.totalCart == 10) {
                                    addImageView.setImageResource(R.drawable.light_add_circle)
                                }
                            }
                        }
                    }
                }
            }
        }

        fun checkAlreadyInList(currentMenu: MenuItem) {
            for (item in DataManager.itemInCartList) {
                if (item != null) {
                    if (item.name == currentMenu.name) {
                        addToCart.visibility = View.GONE
                        addImageView.visibility = View.VISIBLE
                        removeImageView.visibility = View.VISIBLE
                        countTextView.visibility = View.VISIBLE
                        countTextView.text = item.totalCart.toString()
                    }

                }
            }
        }
    }

    interface FoodListClickListener {
        fun addItemToCart(menu: MenuItem)
        fun upgradeItemInCart(menu: MenuItem)
        fun removeItemFromCart(menu: MenuItem)

    }
}