package com.example.group7

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class MainActivity : AppCompatActivity() {
    var restaurant = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val mcdonaldsButton = findViewById<Button>(R.id.mcDonaldsbutton)
        mcdonaldsButton.setOnClickListener {
            val intent = Intent(this, MenuActivity::class.java)
            restaurant = "McDonalds"
            intent.putExtra("restaurant", restaurant)
            startActivity(intent)
        }
        val rootButton = findViewById<Button>(R.id.rootButton)
        rootButton.setOnClickListener {
            restaurant = "rootmenu"
            val intent = Intent(this, MenuActivity::class.java)
            intent.putExtra("restaurant", restaurant)
            startActivity(intent)
        }



        

    }
}