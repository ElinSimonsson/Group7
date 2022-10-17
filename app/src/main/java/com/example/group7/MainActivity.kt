package com.example.group7

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {
    var restaurant = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val mcdonaldsButton = findViewById<Button>(R.id.mcDonaldsbutton)
        mcdonaldsButton.setOnClickListener {
            val intent = Intent(this, MenuActivity::class.java)
            restaurant = "Asian Kitchen menu"
            intent.putExtra("restaurant", restaurant)
            startActivity(intent)
        }
        val rootsButton = findViewById<Button>(R.id.rootButton)
        rootsButton.setOnClickListener {
            restaurant = "Roots & Soil menu"
            val intent = Intent(this, MenuActivity::class.java)
            intent.putExtra("restaurant", restaurant)
            startActivity(intent)
        }
        val primoButton = findViewById<Button>(R.id.primoButton).setOnClickListener {
            restaurant = "Primo Ciao Ciao menu"
            val intent = Intent(this, MenuActivity::class.java)
            intent.putExtra("restaurant", restaurant)
            startActivity(intent)
        }
        

    }
}