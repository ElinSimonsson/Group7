package com.example.group7

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
    //var meals = mutableListOf<Meal>()
    lateinit var meals : ArrayList<Meal>
    var db = Firebase.firestore
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)
      //  createMeals()
        getMenuTest()


//      //  val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
//        recyclerView.layoutManager = GridLayoutManager(this, 2)
////        recyclerView.layoutManager = LinearLayoutManager(this)
//        val adapter = MenuRecycleAdapter(this, meals)
//        recyclerView.adapter = adapter

    }

    fun getMenuData () {
        val restaurant = getRestaurant()
        if (restaurant != null) {
            db.collection(restaurant)
                .get()
                .addOnSuccessListener { result ->
                    for (documents in result) {
                        val name = documents.data.get("name").toString()
                        val price = documents.data.get("price").toString()
                        Log.d("!!!", "$name $price")
                        meals.add(Meal(name, price))
                    }
                }
                .addOnFailureListener{
                    Log.d("!!!", "Fail")
                    meals.add(Meal("This restaurant has no menu","0"))
                }
        }
    }

    fun getMenuTest () {
        db = FirebaseFirestore.getInstance()
        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        meals = arrayListOf()
        db.collection("rootmenu").get()
            .addOnSuccessListener {
                if (!it.isEmpty) {
                    for (data in it.documents) {
                        val meal : Meal? = data.toObject<Meal>(Meal::class.java)
                        meals.add(meal!!)
                    }
                    recyclerView.adapter = MenuRecycleAdapter( meals)
                }
            }
            .addOnFailureListener {
                Toast.makeText(this, it.toString(), Toast.LENGTH_SHORT).show()
            }

    }

    fun getRestaurant(): String? {
        val restaurant = intent.getStringExtra("restaurant")
        return restaurant
    }

//    fun createMeals () {
//        meals.add(Meal("cheeseburger", 80))
//        meals.add(Meal("Big mac", 80))
//        meals.add(Meal("McFeast", 80))
//        meals.add(Meal("Big Tasty", 80))
//        meals.add(Meal("Halloumi burger", 80))
//        meals.add(Meal("Halloumi burger", 80))
//        meals.add(Meal("Halloumi burger", 80))
//        meals.add(Meal("Halloumi burger", 80))
//        meals.add(Meal("Halloumi burger", 80))
//        meals.add(Meal("Halloumi burger", 80))

//    }
}