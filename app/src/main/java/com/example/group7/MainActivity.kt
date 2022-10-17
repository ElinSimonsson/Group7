package com.example.group7

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {
    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val db = Firebase.firestore

        val roots = Restaurant("Roots", 160)
        val primo = Restaurant("Primo Ciao Ciao", 400)
        val asian = Restaurant("Asian Kitchen", 550)


        db.collection("Roots").add(roots)
        db.collection("Primo Ciao Ciao").add(primo)
        db.collection("Asian Kitchen").add(asian)


    }
}