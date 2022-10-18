package com.example.group7

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

import android.widget.Button
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import layout.MyAdapter
import layout.RestaurantDetail

class MainActivity : AppCompatActivity() {

    private lateinit var newRecyclerview : RecyclerView
    private lateinit var newArrayList: ArrayList<RestaurantDetail>
    lateinit var imageid : Array<Int>
    lateinit var heading : Array<String>
    lateinit var heading2 : Array<String>

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        imageid = arrayOf(
            R.drawable.roots,
            R.drawable.primociaociao,
            R.drawable.mama
        )
        heading = arrayOf(
            "Roots and Soil",
            "Primo Ciao Ciao",
            "Asian Kitchen"
        )
        heading2 = arrayOf(
            "Distans : 160m",
            "Distans : 400m",
            "Distans : 550m"
        )
        newRecyclerview = findViewById(R.id.recyclerView)
        newRecyclerview.layoutManager = LinearLayoutManager(this)
        newRecyclerview.setHasFixedSize(true)

        newArrayList = arrayListOf<RestaurantDetail>()
        getUserData()





        /*val button = findViewById<Button>(R.id.button)
        button.setOnClickListener {
            val intent = Intent(this, MenuActivity::class.java)
            startActivity(intent)
        }*/


        val db = Firebase.firestore

        val roots = Restaurant("Roots", 160)
        val primo = Restaurant("Primo Ciao Ciao", 400)
        val asian = Restaurant("Asian Kitchen", 550)


        db.collection("Roots").add(roots)
        db.collection("Primo Ciao Ciao").add(primo)
        db.collection("Asian Kitchen").add(asian)



    }

    private fun getUserData() {
        for(i in imageid.indices){
            val restaurantDetail = RestaurantDetail(imageid[i],heading[i],heading2[i])
            newArrayList.add(restaurantDetail)
        }
        newRecyclerview.adapter = MyAdapter(newArrayList)
    }
}