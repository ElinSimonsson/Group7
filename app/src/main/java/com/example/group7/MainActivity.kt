package com.example.group7


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button

import android.widget.TextView

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore

import com.google.firebase.ktx.Firebase


const val RESTAURANT = "restaurant"

class MainActivity : AppCompatActivity() {
    private lateinit var newRecyclerView: RecyclerView
    private lateinit var newArrayList: ArrayList<RestaurantsData>
    lateinit var imageId: Array<Int>
    lateinit var heading: Array<String>
    lateinit var distance: Array<String>

    lateinit var roots : Button
    lateinit var asian : Button
    lateinit var primo : Button


    lateinit var auth: FirebaseAuth
    lateinit var db : FirebaseFirestore
    lateinit var adressView : TextView




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        auth = Firebase.auth
        auth.signOut()

        db = Firebase.firestore

        imageId = arrayOf(
            R.drawable.roots,
            R.drawable.primo,
            R.drawable.asian,

        )
        heading = arrayOf(
            "Roots and Soil",
            "Primo Ciao Ciao",
            "Asian Kitchen",

        )
        distance = arrayOf(
            "Distans 120m",
            "Distans 400m",
            "Distans 520m",

        )

        newRecyclerView = findViewById(R.id.restaurantRecyclerView)
        newRecyclerView.layoutManager = LinearLayoutManager(this)
        newRecyclerView.setHasFixedSize(true)

        newArrayList = arrayListOf<RestaurantsData>()
        getUserdata()



        adressView = findViewById<TextView>(R.id.adressView)

        getUserAdress {
            adressView.text = it.toString()

        }

        val userBtn = findViewById<Button>(R.id.userBtn)
        userBtn.setOnClickListener{

            val intent = Intent(this, UserActivity::class.java)
            startActivity(intent)
        }

        roots = findViewById(R.id.rootsBtn)
        primo = findViewById(R.id.primoBtn)
        asian = findViewById(R.id.asianBtn)


        asian.setOnClickListener{
             val intent = Intent(this,MenuActivity::class.java)
             intent.putExtra("restaurant","Asian Kitchen")
             startActivity(intent)
         }
         roots.setOnClickListener{
             val intent = Intent(this,MenuActivity::class.java)
             intent.putExtra("restaurant","Roots & Soil")
             startActivity(intent)
         }
         primo.setOnClickListener{
             val intent = Intent(this,MenuActivity::class.java)
             intent.putExtra("restaurant","Primo Ciao Ciao")
             startActivity(intent)
         }



        }


    private fun getUserdata() {
        for (i in imageId.indices) {
            val restaurant = RestaurantsData(imageId[i], heading[i], distance[i])
            newArrayList.add(restaurant)
        }
        newRecyclerView.adapter = RestaurantAdapter(newArrayList)

    }


    override fun onResume() {
        super.onResume()

        Log.d("!!!","user :${auth.currentUser?.email}")
        if(auth.currentUser?.email == "mcdonalds@admin.se"){
            val intentAdmin = Intent(this, AdminActivity::class.java)
            intentAdmin.putExtra(RESTAURANT,"Mcdonalds")
            startActivity(intentAdmin)
            finish()
        }
        getUserAdress {
            adressView.text = it.toString()

        DataManager.itemInCartList.clear()

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
}

private operator fun Button.get(i: Int) {

}







