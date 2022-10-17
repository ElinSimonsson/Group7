package com.example.group7

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.widget.Button
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.getField
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import java.util.*

class MainActivity : AppCompatActivity() {
    lateinit var testButton: Button


    val db = Firebase.firestore
    var name = "McDonalds"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        testButton = findViewById(R.id.testButton)

        testButton.setOnClickListener{
            val intent = Intent(this,orderActivity::class.java)
            startActivity(intent)
        }


        //Aksels Branch


    readData() {
        for(names in it){
            Log.d("!!!",names.name.toString())
        }
    }









    }
    fun readData(myCallback : (List<MenuItem>) -> Unit){
        db.collection(name)
            .get().addOnCompleteListener{ task ->
                if(task.isSuccessful){
                    val list = mutableListOf<MenuItem>()
                    for (document in task.result){
                        val name = document.data["name"].toString()
                        val price = document.data["price"].toString().toInt()
                        val menuItem = MenuItem(name,price)
                        list.add(menuItem)
                    }
                    myCallback(list)
                }


            }
    }




}