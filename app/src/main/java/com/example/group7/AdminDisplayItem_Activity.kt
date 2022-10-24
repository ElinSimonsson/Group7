package com.example.group7

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


const val ITEM_POSITION_NAME = "ITEM_POSITION_NAME"
const val RESTAURANT_NAME = "RestaurantName"

class AdminDisplayItem_Activity : AppCompatActivity() {

    lateinit var editItemName : EditText
    lateinit var editItemPrice : EditText
    lateinit var editItemImage : ImageView
    lateinit var db : FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_display_item)

        editItemName = findViewById(R.id.editItemName)
        editItemPrice = findViewById(R.id.editItemPrice)
        editItemImage = findViewById(R.id.editImageView)
        db = Firebase.firestore


        val restaurantName = intent.getStringExtra(RESTAURANT_NAME)
        val itemPositionName = intent.getStringExtra(ITEM_POSITION_NAME)

        displayItem(restaurantName,itemPositionName)


    }
    fun displayItem (restaurantName : String?,itemName : String?){
        db.collection(restaurantName.toString())
            .whereEqualTo("name",itemName.toString())
            .get()
            .addOnSuccessListener {
                for (document in it){
                    editItemName.setText(document.data["name"].toString())
                    editItemPrice.setText(document.data["price"].toString())
                    Glide.with(this).load(document.data["imageURL"]).into(editItemImage)

                }
            }

    }
}