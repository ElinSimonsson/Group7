package com.example.group7

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore


class AdminActivity : AppCompatActivity() {

    lateinit var recyclerView: RecyclerView
    lateinit var db: FirebaseFirestore
    lateinit var orderButton: TextView
    lateinit var menuButton: TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin)

        menuButton = findViewById(R.id.menuTV)
        orderButton = findViewById(R.id.orderTV)

        replaceWithMenuFragment()

        menuButton.setOnClickListener {
            replaceWithMenuFragment()
        }
        orderButton.setOnClickListener {
            replaceWithOrderFragment()
        }

    }

    private fun replaceWithOrderFragment() {
        val orderFragment = OrderFragment()
        val bundle = Bundle()
        bundle.putString("restaurant", getRestaurantName())
        orderFragment.arguments = bundle
        val fragmentManager = supportFragmentManager
        val transaction = fragmentManager.beginTransaction()
        transaction.replace(R.id.adminContainer, orderFragment)
        transaction.commit()
    }

    private fun replaceWithMenuFragment() {
        val menuFragment = AdminMenuFragment()
        val bundle = Bundle()
        bundle.putString("restaurant", getRestaurantName())
        menuFragment.arguments = bundle
        val fragmentManager = supportFragmentManager
        val transaction = fragmentManager.beginTransaction()
        transaction.replace(R.id.adminContainer, menuFragment)
        transaction.commit()
    }

    fun getRestaurantName(): String {
        val restaurantName = intent.getStringExtra(RES_MAIN).toString()
        return restaurantName
    }
}