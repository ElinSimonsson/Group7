package com.example.group7


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log

import android.widget.Button
import android.widget.TextView

import android.view.View


import androidx.appcompat.app.ActionBar

import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class MenuActivity : AppCompatActivity() {

    lateinit var auth: FirebaseAuth
    lateinit var menuTextView: TextView
    lateinit var drinkTextView : TextView
    lateinit var recyclerView: RecyclerView

    var mainMenuFragment = FoodFragment()
    var mainDrinkFragment = DrinkFragment()


    var db = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)

        auth = Firebase.auth
        menuTextView = findViewById(R.id.menuTextView)
        drinkTextView = findViewById(R.id.drinkTextView)

        menuTextView.setOnClickListener {
            replaceWithFoodFragment()
            Log.d("!!!", "menuTextView körs")
        }
        drinkTextView.setOnClickListener {
            //replaceWithDrinkFragment()
            testDrinkFragment()
        }


        val restaurant = getRestaurantName()
        val actionBar: ActionBar? = supportActionBar
        actionBar?.title = restaurant
        actionBar?.setDisplayHomeAsUpEnabled(true)






//        readData() {
//            recyclerView = findViewById(R.id.menuRecyclerView)
//
//            recyclerView.layoutManager = GridLayoutManager(this, 2)
//            recyclerView.adapter = MenuAdapter(it, this)
//
//        }

//        cartBrn = findViewById(R.id.cartBtn)
//        cartBrn.setOnClickListener {
//
//            getTotalPrice()
//
// }

//        cartTextView = findViewById(R.id.cartTextView)
//        cartTextView.setOnClickListener {
//            val intent = Intent(this, OrderActivity::class.java)
//            startActivity(intent)
//        }

        replaceWithFoodFragment()
    }

    private fun replaceWithDrinkFragment() {
        val restaurant = getRestaurantName()
        val drinkFragment = DrinkFragment()
        //val drinkFragment = DrinkFragment.newInstance(restaurant, restaurant)
       // val fragmentManager = supportFragmentManager
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.container, drinkFragment, "drinkFragment")
        fragmentTransaction.commit()
    }

    fun testDrinkFragment () {
        val fragment = DrinkFragment()
        val bundle = Bundle()
        val restaurant = getRestaurantName()
        bundle.putString("restaurant", restaurant)
        fragment.arguments = bundle
        supportFragmentManager.beginTransaction().replace(R.id.container, fragment)
            .commit()
    }

    fun replaceWithFoodFragment() {
        val foodFragment = FoodFragment()
        val bundle = Bundle()
        val restaurant = getRestaurantName()
        bundle.putString("restaurant", restaurant)
        foodFragment.arguments = bundle
        supportFragmentManager.beginTransaction().replace(R.id.container, foodFragment)
            .commit()
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



//    override fun addItemToCart(menu: MenuItem) {
//        cartTextView.visibility = View.VISIBLE
//        val totalItems = getTotalItems()
//        val price = getTotalPrice()
//        cartTextView.text = getString(R.string.cart_textview, totalItems, price)
//    }
//
//    override fun upgradeItemInCart(menu: MenuItem) {
//        val totalItems = getTotalItems()
//        val price = getTotalPrice()
//        cartTextView.text = getString(R.string.cart_textview, totalItems, price)
//
//    }
//
//    override fun removeItemFromCart(menu: MenuItem) {
//        val totalItems = getTotalItems()
//        val price = getTotalPrice()
//        if(totalItems == 0) {
//            cartTextView.visibility = View.GONE
//        }
//        cartTextView.text = getString(R.string.cart_textview, totalItems, price)
//    }
}



