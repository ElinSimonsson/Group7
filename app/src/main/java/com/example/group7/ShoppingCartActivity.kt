package com.example.group7

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import androidx.appcompat.app.ActionBar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class ShoppingCartActivity : AppCompatActivity(), ShoppingCartRecycleAdapter.listClickListener {
    lateinit var recyclerView: RecyclerView
    lateinit var totalPriceTextView : TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shopping_cart)
        totalPriceTextView = findViewById(R.id.totalPriceTextView2)

        connectRecycleAdapter()

        val restaurant = getRestaurantName()
        val actionBar: ActionBar? = supportActionBar
        actionBar?.title = restaurant
        actionBar?.setDisplayHomeAsUpEnabled(true)

        val totalPrice = getTotalPrice()
        totalPriceTextView.text = "Totalt " + totalPrice.toString() + " kr"




    }

    fun connectRecycleAdapter() {
        recyclerView = findViewById(R.id.shoppingRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        val adapter = ShoppingCartRecycleAdapter(this, DataManager.itemInCartList, this)
        recyclerView.adapter = adapter
    }
    fun getRestaurantName(): String? {
        return intent.getStringExtra("restaurant")
    }
    fun getTotalPrice () : Int {
        var totalPrice = 0
        for (item in DataManager.itemInCartList) {
            totalPrice = if (item?.totalCart!! > 1) {
                val count = item.price?.times(item.totalCart)
                totalPrice + count!!
            } else {
                totalPrice + item.price!!
            }
        }
        return totalPrice
    }

    override fun finishActivity() {
        finish()
    }

    override fun updateInCart() {
        val totalPrice = getTotalPrice()
        totalPriceTextView.text = "Totalt " + totalPrice.toString() + " kr"
//        if(DataManager.itemInCartList.isEmpty()) {
//            finish()
//        }
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            android.R.id.home -> finish()
        }
        return super.onOptionsItemSelected(item)

    }
}