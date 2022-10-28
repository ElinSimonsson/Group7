package com.example.group7

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class AdminActivity : AppCompatActivity() {


    lateinit var recyclerView: RecyclerView
    lateinit var db : FirebaseFirestore
    lateinit var adminMatTextView: TextView
    lateinit var adminDryckTextView: TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin)

        db = Firebase.firestore
        val restaurantName = getRestaurantName()

        val fab = findViewById<FloatingActionButton>(R.id.floatingActionButton)
        fab.setOnClickListener {
            val intentFab = Intent(this,AdminDisplayItem_Activity::class.java)
            intentFab.putExtra("newUser" , 1)
            intentFab.putExtra("restaurantNameFAB",restaurantName)
            startActivity(intentFab)
        }
        adminMatTextView = findViewById(R.id.adminMatTextView)
        adminDryckTextView = findViewById(R.id.adminDryckTextView)

        adminMatTextView.setOnClickListener {
            replaceFragment(AdminMenuFragment())
        }
        adminDryckTextView.setOnClickListener {
            replaceFragment(AdminDrinkFragment())
        }






    }

    private fun replaceFragment(AdminFragment: Fragment) {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frame_layout,AdminFragment)
        fragmentTransaction.commit()
    }


    fun getRestaurantName():String {
        val restaurantName = intent.getStringExtra(RESTAURANT).toString()
        Log.d("!!!","rname fun admin : $restaurantName")
        return restaurantName
    }
}