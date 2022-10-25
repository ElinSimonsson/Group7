package com.example.group7


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import android.util.Log
import android.widget.Button
import android.widget.TextView

import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.ActionBar

import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class MenuActivity : AppCompatActivity(), MenuAdapter.MenuListClickListener {


    lateinit var auth: FirebaseAuth

    lateinit var cartBrn: Button
    lateinit var recyclerView: RecyclerView
    lateinit var cartTextView: TextView


    var db = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)

        auth = Firebase.auth
       // menuAdressTextView = findViewById(R.id.menuAdressView)
        getUserAdress {
       //     menuAdressTextView.text = it.toString()
        }

        backBtn = findViewById(R.id.backBtn)
        backBtn.setOnClickListener {
            finish()
        }


        val restaurant = getRestaurantName()
        val actionBar: ActionBar? = supportActionBar
        actionBar?.title = restaurant
        actionBar?.setDisplayHomeAsUpEnabled(true)
    }


        readMenuData() { menulist->
            recyclerView = findViewById(R.id.menuRecyclerView)

            recyclerView.layoutManager = GridLayoutManager(this, 2)
            recyclerView.adapter = MenuAdapter(it, this)

        }


        cartTextView = findViewById(R.id.cartTextView)
        cartTextView.setOnClickListener {
            val intent = Intent(this, OrderActivity::class.java)
            startActivity(intent)
        }



    fun readData(myCallback: (MutableList<MenuItem>) -> Unit) {
        db.collection("restaurants").document(getRestaurantName()).collection("menu")
            .orderBy("name")
            .get().addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val list = mutableListOf<MenuItem>()
                    for (document in task.result) {
                        val name = document.data["name"].toString()
                        val price = document.data["price"].toString().toInt()
                        val imageURL = document.data["imageURL"].toString()
                        val menuItem = MenuItem(name, price, imageURL, 0)
                        list.add(menuItem)

                    }
                    myCallback(list)
                }
            }
    }



    fun getUserAdress(myCallback : (String) -> Unit){
        db.collection("users").document(auth.currentUser?.uid.toString()).collection("adress")
            .get().addOnCompleteListener{ task ->

                var userAdress = ""
                if(task.isSuccessful){
                    for (document in task.result){
                        val adress = document.data["adress"].toString()
                        userAdress = adress
                    }
                    myCallback(userAdress)
                }
            }
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

    fun getTotalItems () : Int {
        var totalItems = 0
        for (item in DataManager.itemInCartList) {
            totalItems = totalItems + item!!.totalCart
        }
        return totalItems
    }

    fun getRestaurantName(): String {
        val restaurantName = intent.getStringExtra("restaurant").toString()

        return restaurantName
    }

    override fun onOptionsItemSelected(item: android.view.MenuItem): Boolean {

        when (item.itemId) {
            android.R.id.home -> finish()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun addItemToCart(menu: MenuItem) {
        cartTextView.visibility = View.VISIBLE
        val totalItems = getTotalItems()
        val price = getTotalPrice()
        cartTextView.text = getString(R.string.cart_textview, totalItems, price)

    }

    override fun upgradeItemInCart(menu: MenuItem) {
        val totalItems = getTotalItems()
        val price = getTotalPrice()
        cartTextView.text = getString(R.string.cart_textview, totalItems, price)

    }

    override fun removeItemFromCart(menu: MenuItem) {
        val totalItems = getTotalItems()
        val price = getTotalPrice()
        if(totalItems == 0) {
            cartTextView.visibility = View.GONE
        }
        cartTextView.text = getString(R.string.cart_textview, totalItems, price)
    }

}

