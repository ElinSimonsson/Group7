package com.example.group7


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button

import android.widget.TextView
import android.widget.Toast

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore

import com.google.firebase.ktx.Firebase


class MainActivity : AppCompatActivity() {
    private lateinit var newRecyclerView: RecyclerView
    private lateinit var newArrayList: ArrayList<RestaurantsData>
    lateinit var imageId: Array<Int>
    lateinit var heading: Array<String>
    lateinit var distance: Array<String>




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
            "Roots & Soil",
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

        }


    private fun getUserdata() {
        for (i in imageId.indices) {
            val restaurant = RestaurantsData(imageId[i], heading[i], distance[i])
            newArrayList.add(restaurant)
        }

        var adapter = RestaurantAdapter(newArrayList)
        newRecyclerView.adapter = adapter
        adapter.setOnItemClickListener(object : RestaurantAdapter.onItemClickListener{
            override fun onItemClick(position: Int) {

                //Toast.makeText(this@MainActivity,"you clicked on item no. $position", Toast.LENGTH_SHORT).show()

                val intent = Intent(this@MainActivity,MenuActivity::class.java)
                intent.putExtra("restaurant",newArrayList[position].restaurantHeading)
                startActivity(intent)


            }


        })

    }


    override fun onResume() {
        super.onResume()




        Log.d("!!!","user :${auth.currentUser?.email}")
        if(auth.currentUser?.email == "mcdonalds@admin.se"){
            val intentAdmin = Intent(this, AdminActivity::class.java)
            intentAdmin.putExtra(RES_MAIN,"Mcdonalds")
            startActivity(intentAdmin)
            finish()
        }
        if(auth.currentUser?.email == "asiankitchen@admin.se"){
            val intentAdmin = Intent(this, AdminActivity::class.java)
            intentAdmin.putExtra(RES_MAIN,"Asian Kitchen")
            startActivity(intentAdmin)
            finish()
        }
        if(auth.currentUser?.email == "rootssoil@admin.se"){
            val intentAdmin = Intent(this, AdminActivity::class.java)
            intentAdmin.putExtra(RES_MAIN,"Roots & Soil")
            startActivity(intentAdmin)
            finish()
        }
        if(auth.currentUser?.email == "primociaociao@admin.se"){
            val intentAdmin = Intent(this, AdminActivity::class.java)
            intentAdmin.putExtra(RES_MAIN,"Primo Ciao Ciao")
            startActivity(intentAdmin)
            finish()
        }

        getUserAdress {
            adressView.text = it.toString()


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


private operator fun Button.get(i: Int) {

}


}



