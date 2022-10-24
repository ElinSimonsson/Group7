package com.example.group7


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth

import com.google.firebase.ktx.Firebase



class MainActivity : AppCompatActivity() {

    lateinit var auth: FirebaseAuth
    lateinit var mcdonaldsBtn: Button
    lateinit var asianKitchenBtn : Button
    lateinit var rootsSoilBtn : Button
    lateinit var primoCiaoCiaoBtn : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        auth = Firebase.auth


        val userBtn = findViewById<Button>(R.id.userBtn)
        userBtn.setOnClickListener{
            val intent = Intent(this, UserActivity::class.java)
            startActivity(intent)
        }


        mcdonaldsBtn = findViewById(R.id.mcdonaldsBtn)
        asianKitchenBtn = findViewById(R.id.asianKitchenBtn)
        rootsSoilBtn = findViewById(R.id.rootsSoilBtn)
        primoCiaoCiaoBtn = findViewById(R.id.primoCiaoCiaoBtn)



        mcdonaldsBtn.setOnClickListener{
            val intent = Intent(this,MenuActivity::class.java)
            intent.putExtra("restaurant","Mcdonalds")
            startActivity(intent)
        }
        asianKitchenBtn.setOnClickListener{
            val intent = Intent(this,MenuActivity::class.java)
            intent.putExtra("restaurant","Asian Kitchen")
            startActivity(intent)
        }
        rootsSoilBtn.setOnClickListener{
            val intent = Intent(this,MenuActivity::class.java)
            intent.putExtra("restaurant","Roots & Soil")
            startActivity(intent)
        }
        primoCiaoCiaoBtn.setOnClickListener{
            val intent = Intent(this,MenuActivity::class.java)
            intent.putExtra("restaurant","Primo Ciao Ciao")
            startActivity(intent)
        }



    }

    override fun onResume() {
        super.onResume()
        DataManager.itemInCartList.clear()
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