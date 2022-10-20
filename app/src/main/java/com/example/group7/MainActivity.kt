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

        if(auth.currentUser != null){
            Log.d("!!!","${auth.currentUser?.email}")
        }


        var userBtn = findViewById<Button>(R.id.userBtn)
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





}