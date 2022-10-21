package com.example.group7


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore

import com.google.firebase.ktx.Firebase



class MainActivity : AppCompatActivity() {

    lateinit var auth: FirebaseAuth
    lateinit var db : FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        auth = Firebase.auth
        db = Firebase.firestore



        var adressView = findViewById<TextView>(R.id.adressView)

        getUserAdress {
            adressView.text = it.toString()
        }

        val userBtn = findViewById<Button>(R.id.userBtn)
        userBtn.setOnClickListener{
            val intent = Intent(this, UserActivity::class.java)
            startActivity(intent)
        }


       val mcdonaldsBtn = findViewById<Button>(R.id.mcdonaldsBtn)
       val asianKitchenBtn = findViewById<Button>(R.id.asianKitchenBtn)
       val rootsSoilBtn = findViewById<Button>(R.id.rootsSoilBtn)
       val primoCiaoCiaoBtn = findViewById<Button>(R.id.primoCiaoCiaoBtn)


        mcdonaldsBtn.setOnClickListener{
            val intent = Intent(this,MenuActivity::class.java)
            intent.putExtra("restaurant","Mcdonalds")
            startActivity(intent)
        }
        asianKitchenBtn.setOnClickListener{
            val intent = Intent(this,MenuActivity::class.java)
            intent.putExtra("restaurant","Asian Kitchen menu")
            startActivity(intent)
        }
        rootsSoilBtn.setOnClickListener{
            val intent = Intent(this,MenuActivity::class.java)
            intent.putExtra("restaurant","Roots & Soil menu")
            startActivity(intent)
        }
        primoCiaoCiaoBtn.setOnClickListener{
            val intent = Intent(this,MenuActivity::class.java)
            intent.putExtra("restaurant","Primo Ciao Ciao menu")
            startActivity(intent)
        }



    }

    override fun onResume() {
        super.onResume()
            Log.d("!!!","user :${auth.currentUser?.email}")
            if(auth.currentUser?.email == "Admin@Admin.se" || auth.currentUser?.email == "admin@admin.se"){
                val intent = Intent(this, AdminActivity::class.java)
                startActivity(intent)
            } // Admin activity ska vara likadan(ungefär) som menuactivity
        // fast alla varor är editerbara och man ska kunna lägga till ny
        }


    override fun onStart() {
        super.onStart()

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