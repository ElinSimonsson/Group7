package com.example.group7

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners

class DrinkRecycleAdapter(
    var menu: MutableList<MenuItem>,
    val clickListener: DrinkListClickListener
) :
    RecyclerView.Adapter<DrinkRecycleAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {


        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.drink_list_item, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(menu[position])
    }

    override fun getItemCount(): Int {
        return menu.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val nameView = itemView.findViewById<TextView>(R.id.nameTextView1)
        private val priceView = itemView.findViewById<TextView>(R.id.priceTextView1)
        private var menuImage = itemView.findViewById<ImageView>(R.id.menuItemImageView1)
        private var countTextView = itemView.findViewById<TextView>(R.id.amountTextView1)
        private var addImageView = itemView.findViewById<ImageView>(R.id.addImageView1)
        private var removeImageView = itemView.findViewById<ImageView>(R.id.removeImageView1)
        private var addToCart = itemView.findViewById<TextView>(R.id.addToCartButton1)
        private val context = itemView.context


        fun bind(currentMenu: MenuItem) {
            nameView.text = context.getString(R.string.drinkName_textview, currentMenu.name)
            priceView.text = context.getString(R.string.drinkPrice_textview, currentMenu.price)

            checkAlreadyInItemInCartList(currentMenu)

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
                countTextView.text = context.getString(R.string.drinkAmount_textview, currentMenu.totalCart)

                if (currentMenu !in DataManager.itemInCartList) {
                    DataManager.itemInCartList.add(currentMenu)
                    clickListener.addItemToCart(currentMenu)
                }
            }

            removeImageView.setOnClickListener {
                removeImageView.setImageResource(R.drawable.add_circle)

                // En klon av DataManager.itemInCartList skapas
                // för att undvika felkod ConcurrentModificationException

                val cloneList = mutableListOf<MenuItem>()
                for (item in DataManager.itemInCartList) {
                    if (item != null) {
                        cloneList.add(item)
                    }
                }
                var total: Int
                for (item in cloneList) {
                    if (item.name == currentMenu.name) {
                        item.totalCart--
                        total = item.totalCart
                        countTextView.text = context.getString(R.string.drinkAmount_textview, total)
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
                var total: Int
                for (item in DataManager.itemInCartList) {
                    if (item != null) {
                        if (item.name == currentMenu.name) {
                            if (item.totalCart < 10) {
                                item.totalCart++
                                total = item.totalCart
                                countTextView.text = context.getString(R.string.drinkAmount_textview, total)
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

        private fun checkAlreadyInItemInCartList(currentMenu: MenuItem) {
            for (item in DataManager.itemInCartList) {
                if (item != null) {
                    if (item.name == currentMenu.name) {
                        addToCart.visibility = View.GONE
                        addImageView.visibility = View.VISIBLE
                        removeImageView.visibility = View.VISIBLE
                        countTextView.visibility = View.VISIBLE
                        countTextView.text = context.getString(R.string.drinkAmount_textview, item.totalCart)
                    }
                }
            }
        }
    }

    interface DrinkListClickListener {
        fun addItemToCart(menu: MenuItem)
        fun upgradeItemInCart(menu: MenuItem)
        fun removeItemFromCart(menu: MenuItem)

    }
}