package com.example.group7

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.widget.Button
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.getField
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {
    lateinit var testButton: Button


    val db = Firebase.firestore
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        testButton = findViewById(R.id.testButton)

        testButton.setOnClickListener{
            val intent = Intent(this,orderActivity::class.java)
            startActivity(intent)
        }

        var name = "McDonalds"
        //Aksels Branch

        var menu = initializeMenu(name)





        for (items in menu){
            Log.d("!!!","${items.price}")
        }





    //  val cities = db.collection("cities")

    //  val data1 = hashMapOf(
    //      "name" to "San Fransisco",
    //      "state" to "CA",
    //      "country" to "USA",
    //      "capital" to false,
    //      "population" to 860000,
    //      "regions" to listOf("west_coast", "norcal")
    //  )
    //  cities.document("SF").set(data1)

    //  val data2 = hashMapOf(
    //      "name" to "Washington D.C",
    //      "state" to null,
    //      "country" to "USA",
    //      "capital" to true,
    //      "population" to 680000,
    //      "regions" to listOf("east_coast")
    //  )
    //  cities.document("DC").set(data2)

    //   val docRef = db.collection("cities").document("SF")
    //   docRef.get()
    //       .addOnSuccessListener { document ->
    //           if (document != null){
    //               Log.d("!!!", "DocumentSnapShotData : ${document.data}")
    //           }
    //           else{
    //               Log.d("!!!", "No such document")
    //           }
    //       }
    //       .addOnFailureListener{ exception ->
    //           Log.d("!!!", "get failed with" , exception)

    //       }
       // db.collection(restaurantName)
//
       //     .get()
       //     .addOnSuccessListener { documents ->
       //         for (document in documents) {
       //             Log.d("!!!", "${document.id} => ${document.data}")
       //         }
       //     }
       //     .addOnFailureListener { exception ->
       //         Log.w("!!!", "Error getting documents: ", exception)
       //     }




    }
    fun initializeMenu(name:String) : MutableList<MenuItem>{
        val menu = mutableListOf<MenuItem>()
        db.collection(name)

            .get()
            .addOnSuccessListener { result ->
                for (documents in result){
                    val name = documents.data.get("name").toString()
                    val price = documents.data.get("price").toString().toInt()
                    menu.add(MenuItem(name,price))

                }

            }
            .addOnFailureListener{
                menu.add(MenuItem("This restaurant has no menu",0))
            }
        return menu
    }


    
}