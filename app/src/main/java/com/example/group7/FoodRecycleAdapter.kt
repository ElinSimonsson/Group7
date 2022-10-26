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
            LayoutInflater.from(parent.context).inflate(R.layout.food_list_item, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(menu.get(position))
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
            for(item in DataManager.itemInCartList) {
                if (item != null) {
                    Log.d("!!!", "${item.name}")

                }
                Log.d("!!!", "nuvarande menu = $currentMenu")
            }

            nameView.text = currentMenu.name
            priceView.text = "${currentMenu.price} kr"


            for(item in DataManager.itemInCartList) {
                if (item != null) {
                    if(item.name == currentMenu.name) {
                        Log.d("!!!", "for loop körs, $currentMenu finns i listan")
                        addToCart.visibility = View.GONE
                        addImageView.visibility = View.VISIBLE
                        removeImageView.visibility = View.VISIBLE
                        countTextView.visibility = View.VISIBLE
                        countTextView.text = item.totalCart.toString()
                    }

                }
            }

//              if(DataManager.itemInCartList.contains(currentMenu)) {
//                  Log.d("!!!", "$currentMenu finns i listan")
//                addToCart.visibility = View.GONE
//                addImageView.visibility = View.VISIBLE
//                removeImageView.visibility = View.VISIBLE
//                countTextView.visibility = View.VISIBLE
//
//                  countTextView.text = currentMenu.totalCart.toString()
//            }



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
                var total1 = 0
                var total = 0
                for(item in DataManager.itemInCartList) {
                    if (item != null) {
                        item.totalCart--
                        total1 = item.totalCart
                        countTextView.text = total1.toString()
                        Log.d("!!!", "Tidigt test, total ${item.totalCart}")
//                        if(item.name == currentMenu.name) {
//                           // currentMenu.totalCart = item.totalCart
//                           // total1 = currentMenu.totalCart
//                            //Log.d("!!!", "nuvarande vara total: $total1")
//                            //Log.d("!!!", "total1 $total1")
//
//                           // countTextView.text = total1.toString()
//                        }
//                    } else {
//                        Log.d("!!!", "Remove körs, varan finns inte i listan")
//
//                    }
                        //total1 = currentMenu.totalCart
                        //total--
                    }
                }
              //  total1--
                Log.d("!!!", "test total $total1")
               // countTextView.text = total1.toString()

                if (total1 > 0) {
                    //total1--
                    Log.d("!!!", "if körs, total: $total1")
                    currentMenu.totalCart = total1
                    countTextView.text = total1.toString()
                    //countTextView.text = currentMenu.totalCart.toString()
                    val index = DataManager.itemInCartList.indexOf(currentMenu)
                    DataManager.itemInCartList.removeAt(index)
                    DataManager.itemInCartList.add(currentMenu)
                    clickListener.upgradeItemInCart(currentMenu)

                } else {
                  //  total1--
                    Log.d("!!!", "else körs")
                    //currentMenu.totalCart = total1
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

        fun getTotalItems () : Int {
            var totalItems = 0
            for (item in DataManager.itemInCartList) {
                totalItems = totalItems + item!!.totalCart
            }
            return totalItems
        }
    }

    interface MenuListClickListener {
        fun addItemToCart (menu: MenuItem)
        fun upgradeItemInCart (menu: MenuItem)
        fun removeItemFromCart(menu: MenuItem)

    }
}