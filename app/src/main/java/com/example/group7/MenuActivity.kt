package com.example.group7

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MenuActivity : AppCompatActivity() {
    val meals = mutableListOf<Meal>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)
        createMeals()


        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        val adapter = MenuRecycleAdapter(this, meals)
        recyclerView.adapter = adapter

    }

    fun createMeals () {
        meals.add(Meal("cheeseburger", 80))
        meals.add(Meal("Big mac", 80))
        meals.add(Meal("McFeast", 80))
        meals.add(Meal("Big Tasty", 80))
        meals.add(Meal("Halloumi burger", 80))

    }
}