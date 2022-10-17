package com.example.group7

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class MainActivity : AppCompatActivity() {
    lateinit var testButton: Button
    lateinit var menuButton: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        testButton = findViewById(R.id.orderBtn)
        menuButton = findViewById(R.id.menuBtn)

        testButton.setOnClickListener{
            val intent = Intent(this,orderActivity::class.java)
            startActivity(intent)
        }

        menuButton.setOnClickListener{
            val intent = Intent(this,MenuActivity::class.java)
            startActivity(intent)
        }


        //Aksels Branch




    }





}