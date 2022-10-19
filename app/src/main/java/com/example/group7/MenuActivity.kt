package com.example.group7


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View.inflate
import android.widget.Button
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import com.example.group7.databinding.ActivityMenuBinding
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class MenuActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMenuBinding

    lateinit var backBtn : Button
    lateinit var cartBrn : Button
    var recyclerView : RecyclerView? = null
    var Manager: LinearLayoutManager?  = null
    var adapter: MenuAdapter? = null

    var db = Firebase.firestore
    var meals = mutableListOf<MenuItem>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.menuRecyclerView.apply {
             layoutManager = LinearLayoutManager(this@MenuActivity)
        }

//        recyclerView = findViewById(R.id.menuRecyclerView) as RecyclerView
//        Manager = LinearLayoutManager(this)
//        recyclerView!!.layoutManager = Manager
//        adapter = MenuAdapter(this@MenuActivity, meals)
//        recyclerView!!.adapter = adapter

       // setContentView(R.layout.activity_menu)

        backBtn = findViewById(R.id.backBtn)
        backBtn.setOnClickListener{
           // intent = Intent(this, MainActivity::class.java)
            finish()
           // startActivity(intent)
        }

        fetchData()



//        readData() {
//            recyclerView = findViewById(R.id.menuRecyclerView)
//            recyclerView.layoutManager = LinearLayoutManager(this@MenuActivity)
//            recyclerView.adapter = MenuAdapter(this, it)
//        }
//
        cartBrn = findViewById(R.id.cartBtn)
        cartBrn.setOnClickListener{
            intent = Intent(this, orderActivity::class.java)
            startActivity(intent)
        }

    }

    fun readData(myCallback : (MutableList<MenuItem>) -> Unit){
            db.collection(getRestaurantName())
                .get().addOnCompleteListener{ task ->
                    if(task.isSuccessful){
                        val list = mutableListOf<MenuItem>()
                        for (document in task.result){
                            val name = document.data["name"].toString()
                            val price = document.data["price"].toString().toInt()
                            val imageURL = document.data["imageURL"].toString()
                            val menuItem = MenuItem(name,price, imageURL)
                            list.add(menuItem)
                        }
                        myCallback(list)
                    }
                }
            }

    fun getRestaurantName():String {
        val restaurantName = intent.getStringExtra("restaurant").toString()
        return restaurantName
    }

    fun fetchData () {
        FirebaseFirestore.getInstance().collection("Roots & Soil menu")
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    val menu = documents.toObjects(MenuItem::class.java)
                    Log.d("!!!", "data: ${document.data["imageURL"]}, ${document.data["name"]}")
                    binding.menuRecyclerView.adapter = MenuAdapter(this, menu)
//                    val name = document.data["name"].toString()
//                    val price = document.data["price"].toString().toInt()
//                    val imageURL = document.data["imageURL"].toString()

                }

            }
            .addOnFailureListener {
                Toast.makeText(this, it.toString(), Toast.LENGTH_SHORT).show()
            }
    }
    fun getMenuTest () {
        val restaurant = getRestaurant()
        if (restaurant != null) {
            db = FirebaseFirestore.getInstance()
            recyclerView = findViewById(R.id.menuRecyclerView)
            recyclerView!!.layoutManager = LinearLayoutManager(this)
            meals = mutableListOf()
            db.collection("Roots & Soil menu").get()
                .addOnSuccessListener {
                    if (!it.isEmpty) {
                        for (data in it.documents) {
                            val meal: MenuItem? = data.toObject(MenuItem::class.java)
                            meals.add(meal!!)
                        }
                        recyclerView!!.adapter = MenuAdapter(this, meals)
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

