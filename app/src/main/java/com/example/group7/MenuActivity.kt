package com.example.group7

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class MenuActivity : AppCompatActivity() {

    val db = Firebase.firestore
    var name = "McDonalds"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)


        readData() {
            for(names in it){
                Log.d("!!!",names.price.toString())
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