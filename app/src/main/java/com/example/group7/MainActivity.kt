package com.example.group7

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

//Aksel Branch
class MainActivity : AppCompatActivity() {
    lateinit var mcdonaldsBtn: Button
    lateinit var asianKitchenBtn : Button
    lateinit var rootsSoilBtn : Button
    lateinit var primoCiaoCiaoBtn : Button

    val db = Firebase.firestore


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)



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