package com.example.group7

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase

class OrderActivity : AppCompatActivity() {
    lateinit var db : FirebaseFirestore
    lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order)

        db = Firebase.firestore
        fetchDocumentIdData {
            recyclerView = findViewById(R.id.orderRecyclerView)
            recyclerView.layoutManager = LinearLayoutManager(this)
            val adapter = OrderRecycleAdapter(it, "hej")
            recyclerView.adapter = adapter
        }


//        val listOfDocumentId = mutableListOf<DocumentId>()

//        val docRef = db.collection("Orders").document("Mcdonalds")
//            .collection("Orders")
//
//        docRef.addSnapshotListener { snapshot, e ->
//            if (snapshot != null) {
//                Log.d("!!!", "item updated!")
//                for(document in snapshot.documents) {
//                    val documentId = document.id
//                    Log.d("!!!", "Testar att skriva ut ID: $documentId")
//                    val id = DocumentId(documentId)
//                    listOfDocumentId.add(id)

//                    val docref1 = db.collection("Orders").document("Mcdonalds")
//                        .collection("Orders").document(documentId).collection("Order")
//
//                    val orderList = mutableListOf<Order>()
//
//                    docref1.addSnapshotListener { snapshot1, _ ->
//                        if(snapshot1 != null) {
//                            orderList.clear()
//                            for(item in snapshot1.documents) {
//                                val order = item.toObject<Order>()
//                                if(order!= null) {
//                                    orderList.add(order)
//                                }
//                            }
//                            for(item in orderList) {
//                                Log.d("!!!", "$item")
//                            }
//                        }
//
//                    }
//                }
//            }
//        }
    }
    fun readMenuData(myCallback : (MutableList<AdminMenuItem>) -> Unit){
        Log.d("!!!","Fun rmd DRINK")
        db.collection(RESTAURANT_STRING)
            .document("Mcdonalds")
            .collection(DRINK)
            .get()
            .addOnCompleteListener{ task ->
                if(task.isSuccessful){
                    val list = mutableListOf<AdminMenuItem>()
                    for (document in task.result){
                        val name = document.data["name"].toString()
                        val price = document.data["price"].toString().toInt()
                        val imageURL = document.data["imageURL"].toString()
                        val documentID = document.id
                        val adminMenuItem = AdminMenuItem(documentID,name,price, imageURL)
                        list.add(adminMenuItem)
                    }
                    myCallback(list)
                }
            }
    }

    fun fetchDocumentIdData (myCallback : (MutableList<DocumentId>)-> Unit) {

        val docRef = db.collection("Orders").document("Mcdonalds")
            .collection("Orders")

        docRef.addSnapshotListener { snapshot, e ->
            if (snapshot != null) {
                Log.d("!!!", "item updated!")
                val listOfDocumentId = mutableListOf<DocumentId>()
                for(document in snapshot.documents) {
                    val documentId = document.id
                    Log.d("!!!", "Testar att skriva ut ID: $documentId")
                    val id = DocumentId(documentId)
                    listOfDocumentId.add(id)
                }
                myCallback(listOfDocumentId)
            }
        }
    }
}