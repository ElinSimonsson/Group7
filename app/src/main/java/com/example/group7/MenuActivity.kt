package com.example.group7


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class MenuActivity : AppCompatActivity() {

    lateinit var backBtn : Button
    lateinit var cartBrn : Button
    lateinit var menuAdressTextView: TextView
    lateinit var recyclerView : RecyclerView

    lateinit var auth: FirebaseAuth

    var db = Firebase.firestore


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)

        auth = Firebase.auth


        menuAdressTextView = findViewById(R.id.menuAdressView)
        getUserAdress {
            menuAdressTextView.text = it.toString()
        }

        backBtn = findViewById(R.id.backBtn)
        backBtn.setOnClickListener{
            finish()
        }


        readMenuData() {
            recyclerView = findViewById(R.id.menuRecyclerView)
            recyclerView.layoutManager = GridLayoutManager(this@MenuActivity, 2)
            recyclerView.adapter = MenuAdapter(this, it)
        }

        cartBrn = findViewById(R.id.cartBtn)
        cartBrn.setOnClickListener{
            intent = Intent(this, orderActivity::class.java)
            startActivity(intent)
        }

    }

    fun readMenuData(myCallback : (MutableList<MenuItem>) -> Unit){
            db.collection(getRestaurantName())
                .get()
                .addOnCompleteListener{ task ->
                    if(task.isSuccessful){
                        val list = mutableListOf<MenuItem>()
                        for (document in task.result){
                            val name = document.data["name"].toString()
                            Log.d("!!!","$name")
                            val price = document.data["price"].toString().toInt()
                            val imageURL = document.data["imageURL"].toString()
                            val menuItem = MenuItem(name,price, imageURL)
                            list.add(menuItem)
                        }
                        myCallback(list)
                    }
                }
            }
    fun getUserAdress(myCallback : (String) -> Unit){
        db.collection("users").document(auth.currentUser?.uid.toString()).collection("adress")
            .get().addOnCompleteListener{ task ->

                var userAdress = ""
                if(task.isSuccessful){
                    for (document in task.result){
                        val adress = document.data["adress"].toString()
                        userAdress = adress
                    }
                    myCallback(userAdress)
                }
            }
    }

    fun getRestaurantName():String {
        val restaurantName = intent.getStringExtra(RESTAURANT).toString()
        Log.d("!!!","R name main : $restaurantName")
        return restaurantName
    }

}

