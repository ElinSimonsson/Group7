package com.example.group7

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.getField
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {

    val db = Firebase.firestore
    val restaurantName = "McDonalds"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Aksels Branch
      getMenu(restaurantName)




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

       val menu = mutableMapOf<String, Int>()



    }
    fun getMenu(name : String) {



        db.collection(name)

            .get()
            .addOnSuccessListener { result ->
                for (documents in result){
                    Log.d("!!!","${documents.id} => ${documents.data}")

                }

            }
            .addOnFailureListener{ exception->
                Log.d("!!!","ERRor", exception)
            }
    }
}