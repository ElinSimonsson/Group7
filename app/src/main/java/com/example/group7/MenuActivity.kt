package com.example.group7


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log

import android.widget.Button
import android.widget.TextView

import android.view.View


import androidx.appcompat.app.ActionBar
import androidx.recyclerview.widget.GridLayoutManager

import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class MenuActivity : AppCompatActivity() {

    lateinit var auth: FirebaseAuth

    lateinit var menuTextView: TextView
    lateinit var drinkTextView : TextView

    lateinit var recyclerView: RecyclerView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)

        auth = Firebase.auth



      val menuAdressTextView = findViewById<TextView>(R.id.adressTextView)
       getUserAdress {
           menuAdressTextView.text = it.toString()
       }


        menuTextView = findViewById(R.id.menuTextView)
        drinkTextView = findViewById(R.id.drinkTextView)



        val restaurant = getRestaurantName()
        val actionBar: ActionBar? = supportActionBar
        actionBar?.title = restaurant
        actionBar?.setDisplayHomeAsUpEnabled(true)

        replaceWithFoodFragment()

            menuTextView.setOnClickListener {
                replaceWithFoodFragment()
            }
               drinkTextView.setOnClickListener {

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

    fun getRestaurantName(): String {
        val restaurantName = intent.getStringExtra("restaurant").toString()

        return restaurantName
    }


         
    fun replaceWithDrinkFragment () {
        val fragment = DrinkFragment()
        val bundle = Bundle()
        val restaurant = getRestaurantName()
        bundle.putString("restaurant", restaurant)
        fragment.arguments = bundle
        supportFragmentManager.beginTransaction().replace(R.id.container, fragment)
            .commit()
    }

    fun replaceWithFoodFragment() {
        val foodFragment = FoodFragment()
        val bundle = Bundle()
        val restaurant = getRestaurantName()
        bundle.putString("restaurant", restaurant)
        foodFragment.arguments = bundle
        supportFragmentManager.beginTransaction().replace(R.id.container, foodFragment)
            .commit()
    }

     override fun onOptionsItemSelected(item: android.view.MenuItem): Boolean {

        when (item.itemId) {
            android.R.id.home -> finish()
        }
        return super.onOptionsItemSelected(item)
    }
}




