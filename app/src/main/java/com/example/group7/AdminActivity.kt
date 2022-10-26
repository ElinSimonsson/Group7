package com.example.group7

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlin.math.log


class AdminActivity : AppCompatActivity() {


    lateinit var recyclerView: RecyclerView
    lateinit var db : FirebaseFirestore


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

        readMenuData {
            recyclerView = findViewById(R.id.AdminMenuRecyclerView)
            recyclerView.layoutManager = GridLayoutManager(this@AdminActivity,2)
            val adapter = AdminMenuAdapter(this,it,restaurantName)
            recyclerView.adapter = adapter
        }



    }

    fun readMenuData(myCallback : (MutableList<AdminMenuItem>) -> Unit){
        db.collection(RESTAURANT_STRING)
            .document(getRestaurantName())
            .collection(MENU)
            .get()
            .addOnCompleteListener{ task ->
                if(task.isSuccessful){
                    val list = mutableListOf<AdminMenuItem>()
                    for (document in task.result){
                        val name = document.data["name"].toString()
                        val price = document.data["price"].toString().toInt()
                        val imageURL = document.data["imageURL"].toString()
                        val documentID = document.id
                        val adminMenuItem = AdminMenuItem(documentID,name,price, imageURL)
                        list.add(adminMenuItem)
                    }
                    myCallback(list)
                }
            }
    }

    fun getRestaurantName():String {
        val restaurantName = intent.getStringExtra(RESTAURANT).toString()
        Log.d("!!!","rname fun admin : $restaurantName")
        return restaurantName
    }
}