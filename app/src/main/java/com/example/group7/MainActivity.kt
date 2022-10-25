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


const val RESTAURANT = "restaurant"

class MainActivity : AppCompatActivity() {

    lateinit var auth: FirebaseAuth
    lateinit var db : FirebaseFirestore
    lateinit var adressView : TextView



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        auth = Firebase.auth
        //auth.signOut()

        db = Firebase.firestore

        adressView = findViewById<TextView>(R.id.adressView)

       val mcdonaldsBtn = findViewById<Button>(R.id.mcdonaldsBtn)
       val asianKitchenBtn = findViewById<Button>(R.id.asianKitchenBtn)
       val rootsSoilBtn = findViewById<Button>(R.id.rootsSoilBtn)
       val primoCiaoCiaoBtn = findViewById<Button>(R.id.primoCiaoCiaoBtn)

        getUserAdress {
            adressView.text = it.toString()

        }

        val userBtn = findViewById<Button>(R.id.userBtn)
        userBtn.setOnClickListener{
            val intent = Intent(this, UserActivity::class.java)
            startActivity(intent)
        }

        mcdonaldsBtn.setOnClickListener{
            val intent = Intent(this,MenuActivity::class.java)
            intent.putExtra(RESTAURANT,"Mcdonalds")
            startActivity(intent)
        }
        asianKitchenBtn.setOnClickListener{
            val intent = Intent(this,MenuActivity::class.java)
            intent.putExtra(RESTAURANT,"Asian Kitchen menu")
            startActivity(intent)
        }
        rootsSoilBtn.setOnClickListener{
            val intent = Intent(this,MenuActivity::class.java)
            intent.putExtra(RESTAURANT,"Roots & Soil menu")
            startActivity(intent)
        }
        primoCiaoCiaoBtn.setOnClickListener{
            val intent = Intent(this,MenuActivity::class.java)
            intent.putExtra(RESTAURANT,"Primo Ciao Ciao menu")
            startActivity(intent)
        }



    }

    override fun onResume() {
        super.onResume()

        getUserAdress {
            adressView.text = it.toString()
        }

            Log.d("!!!","user :${auth.currentUser?.email}")
            if(auth.currentUser?.email == "mcdonalds@admin.se"){
                val intentAdmin = Intent(this, AdminActivity::class.java)
                intentAdmin.putExtra(RESTAURANT,"Mcdonalds")
                startActivity(intentAdmin)
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