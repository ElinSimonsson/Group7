package com.example.group7


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth

import com.google.firebase.ktx.Firebase



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





    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)




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

        auth = Firebase.auth


        var userBtn = findViewById<Button>(R.id.userBtn)
        userBtn.setOnClickListener {
            val intent = Intent(this, UserActivity::class.java)
            startActivity(intent)
        }

        roots = findViewById(R.id.rootsBtn)
        primo = findViewById(R.id.primoBtn)
        asian = findViewById(R.id.asianBtn)






        asian.setOnClickListener{
             val intent = Intent(this,MenuActivity::class.java)
             intent.putExtra("restaurant","Asian Kitchen menu")
             startActivity(intent)
         }
         roots.setOnClickListener{
             val intent = Intent(this,MenuActivity::class.java)
             intent.putExtra("restaurant","Roots & Soil menu")
             startActivity(intent)
         }
         primo.setOnClickListener{
             val intent = Intent(this,MenuActivity::class.java)
             intent.putExtra("restaurant","Primo Ciao Ciao menu")
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


    //user
    override fun onResume() {
        super.onResume()
        if(auth.currentUser != null){
            Log.d("!!!","user :${auth.currentUser?.email}")
            if(auth.currentUser?.email == "Admin@Admin.se"){

            }
        }
    }

    override fun onStart() {
        super.onStart()
        auth.signOut()


    }
}

private operator fun Button.get(i: Int) {

}



