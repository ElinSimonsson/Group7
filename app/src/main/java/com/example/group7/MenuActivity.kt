package com.example.group7


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class MenuActivity : AppCompatActivity() {


    lateinit var backBtn : Button
    lateinit var cartBrn : Button
    lateinit var recyclerView : RecyclerView
    val db = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_menu)

        backBtn = findViewById(R.id.backBtn)
        backBtn.setOnClickListener{
           // intent = Intent(this, MainActivity::class.java)
            finish()
           // startActivity(intent)
        }



        readData() {
            recyclerView = findViewById(R.id.menuRecyclerView)
            recyclerView.layoutManager = LinearLayoutManager(this@MenuActivity)
            recyclerView.adapter = MenuAdapter(this, it)
        }
        
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
        FirebaseFirestore.getInstance().collection(getRestaurantName())
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    val name = document.data["name"].toString()
                    val price = document.data["price"].toString().toInt()
                    val imageURL = document.data["imageURL"].toString()

                }

            }
            .addOnFailureListener {

            }
    }

}

