package com.example.group7

import android.app.ProgressDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase

class MenuActivity : AppCompatActivity() {
    private lateinit var recyclerView : RecyclerView
    lateinit var meals : MutableList<Meal>
    var db = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)

        getMenuTest()

    }

    fun getMenuTest () {
        val restaurant = getRestaurant()
            if (restaurant != null) {
                db = FirebaseFirestore.getInstance( )
                recyclerView = findViewById(R.id.recyclerView)
                recyclerView.layoutManager = GridLayoutManager(this, 2)
                meals = mutableListOf()
                db.collection(restaurant).get()
                    .addOnSuccessListener {
                        if (!it.isEmpty) {
                            for (data in it.documents) {
                                val meal: Meal? = data.toObject(Meal::class.java)
                                meals.add(meal!!)
                            }
                            recyclerView.adapter = MenuRecycleAdapter(meals)
                        }
                    }
                    .addOnFailureListener {
                        Toast.makeText(this, it.toString(), Toast.LENGTH_SHORT).show()
                    }

            }
    }

    fun getRestaurant(): String? {
        val restaurant = intent.getStringExtra("restaurant")
        return restaurant
    }
}

