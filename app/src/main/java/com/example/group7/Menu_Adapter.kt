package com.example.group7

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners

class MenuAdapter(var menu: MutableList<MenuItem>, val clickListener: MenuListClickListener) :
    RecyclerView.Adapter<MenuAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {


        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.menu_list_item, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(menu.get(position))
    }

    override fun getItemCount(): Int {
        return menu.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameView = itemView.findViewById<TextView>(R.id.nameTextView1)
        val priceView = itemView.findViewById<TextView>(R.id.priceTextView1)
        var menuImage = itemView.findViewById<ImageView>(R.id.menuItemImageView)
        var countTextView = itemView.findViewById<TextView>(R.id.amountTextView)
        var addImageView = itemView.findViewById<ImageView>(R.id.addImageView)
        var removeImageView = itemView.findViewById<ImageView>(R.id.removeImageView)
        var addToCart = itemView.findViewById<TextView>(R.id.addToCartButton)


        fun bind(currentMenu: MenuItem) {
            nameView.text = currentMenu.name
            priceView.text = "${currentMenu.price} kr"

            if(DataManager.itemInCartList.contains(currentMenu)) {
                addToCart.visibility = View.GONE
                addImageView.visibility = View.VISIBLE
                removeImageView.visibility = View.VISIBLE
                countTextView.visibility = View.VISIBLE
            }

            addToCart.setOnClickListener {
                addToCart.visibility = View.GONE
                addImageView.visibility = View.VISIBLE
                removeImageView.visibility = View.VISIBLE
                countTextView.visibility = View.VISIBLE
                currentMenu.totalCart++
                countTextView.text = currentMenu.totalCart.toString()
                //clickListener.addItemToCart(currentMenu)

                if(currentMenu !in DataManager.itemInCartList) {
                    DataManager.itemInCartList.add(currentMenu)
                    clickListener.addItemToCart(currentMenu)
                }
            }

            removeImageView.setOnClickListener {
                var total: Int = currentMenu.totalCart
                total--
                if (total > 0) {
                    currentMenu.totalCart = total
                    countTextView.text = currentMenu.totalCart.toString()
                    val index = DataManager.itemInCartList.indexOf(currentMenu)
                    DataManager.itemInCartList.removeAt(index)
                    DataManager.itemInCartList.add(currentMenu)
                    clickListener.upgradeItemInCart(currentMenu)

                } else {
                    currentMenu.totalCart = total
                    addImageView.visibility = View.GONE
                    removeImageView.visibility = View.GONE
                    countTextView.visibility = View.GONE
                    addToCart.visibility = View.VISIBLE

                    if(DataManager.itemInCartList.contains(currentMenu)) {
                        DataManager.itemInCartList.remove(currentMenu)
                        clickListener.removeItemFromCart(currentMenu)
                    }
                }
            }

            addImageView.setOnClickListener {
                var total: Int = currentMenu.totalCart
                total++
                if (total <= 10) {
                    currentMenu.totalCart = total
                    countTextView.text = currentMenu.totalCart.toString()
                    clickListener.addItemToCart(currentMenu)

                    if (currentMenu !in DataManager.itemInCartList) {
                        DataManager.itemInCartList.add(currentMenu)
                    }
                }
            }

            val radius = 30
            Glide.with(menuImage)
                .load(currentMenu.imageURL)
                .error(R.drawable.ic_launcher_background)
                .centerCrop()
                .transform(RoundedCorners(radius))
                .into(menuImage)
        }
    }

    interface MenuListClickListener {
        fun addItemToCart (menu: MenuItem)
        fun upgradeItemInCart (menu: MenuItem)
        fun removeItemFromCart(menu: MenuItem)

    }
}