package com.example.group7

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase


class AdminActivity : AppCompatActivity() {

    lateinit var recyclerView: RecyclerView
    lateinit var db: FirebaseFirestore
    lateinit var auth : FirebaseAuth
    lateinit var orderButton: TextView
    lateinit var menuButton: TextView
    lateinit var logOutBtn : TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin)

        auth = Firebase.auth

        menuButton = findViewById(R.id.menuTV)
        orderButton = findViewById(R.id.orderTV)
        logOutBtn = findViewById(R.id.logOutBtn)

        replaceWithMenuFragment()

        menuButton.setOnClickListener {
            replaceWithMenuFragment()
        }
        orderButton.setOnClickListener {
            replaceWithOrderFragment()
        }
        logOutBtn.setOnClickListener {
            val intent = Intent(this,MainActivity::class.java)
            auth.signOut()
            finish()
            startActivity(intent)
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