package com.example.group7


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.LinearLayout
import androidx.appcompat.app.ActionBar
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class MenuActivity : AppCompatActivity() {

    lateinit var backBtn: Button
    lateinit var cartBrn: Button
    lateinit var recyclerView: RecyclerView

    var totalPrice = 0

    var db = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)

        backBtn = findViewById(R.id.backBtn)
        backBtn.setOnClickListener {
            finish()
        }

        val restaurant = getRestaurantName()
        val actionBar: ActionBar? = supportActionBar
        actionBar?.setTitle(restaurant)
        actionBar?.setDisplayHomeAsUpEnabled(true)

        readData() {
            recyclerView = findViewById(R.id.menuRecyclerView)
            recyclerView.layoutManager = LinearLayoutManager(this)
            recyclerView.adapter = MenuAdapter(it)
        }

        cartBrn = findViewById(R.id.cartBtn)
        cartBrn.setOnClickListener {

            for (item in DataManager.itemInCartList) {
                Log.d("!!!", "${item.name} och antal ${item.totalCart}")
                if (item.totalCart > 1) {
                    var price = item.price
                    var antal = item.totalCart
                    var count = price?.times(antal)
                    totalPrice = totalPrice + count!!
                } else {
                    totalPrice = totalPrice + item.price!!
                }

            }
            Log.d("!!!", "Totalpris $totalPrice")

            intent = Intent(this, orderActivity::class.java)
            startActivity(intent)
        }

    }

    override fun onResume() {
        super.onResume()
        totalPrice = 0
    }

    fun readData(myCallback: (MutableList<MenuItem>) -> Unit) {
        db.collection(getRestaurantName())
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

    fun getRestaurantName(): String {
        val restaurantName = intent.getStringExtra("restaurant").toString()
        return restaurantName
    }

}

