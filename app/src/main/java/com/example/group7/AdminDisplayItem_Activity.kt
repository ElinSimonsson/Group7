package com.example.group7

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
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
        val saveBtn = findViewById<Button>(R.id.saveBtn)
        val deleteBtn = findViewById<Button>(R.id.deleteBtn)
        db = Firebase.firestore

        val restaurantName = getRestaurant().toString()
        val itemPositionName = getItemPosition().toString()
        Log.d("!!!","$restaurantName")


        displayItem(restaurantName,itemPositionName)


       //if(itemPositionName == null){
       //    saveBtn.setOnClickListener {
       //        newItem(restaurantName)
       //    }
       //    deleteBtn.setOnClickListener {

       //    }

       //}
       //else{
       //    updateItem(restaurantName,itemPositionName)
       //}




    }
    fun displayItem (restaurantName : String,itemName : String){
        Log.d("!!!","DN : $restaurantName")
        db.collection(restaurantName)
            .whereEqualTo("name",itemName)
            .get()
            .addOnSuccessListener {
                for (document in it){
                    editItemName.setText(document.data["name"].toString())
                    Log.d("!!!","name : ${editItemName.text}")
                    editItemPrice.setText(document.data["price"].toString())
                    Glide.with(this).load(document.data["imageURL"]).into(editItemImage)

                }
            }

    }
    fun newItem(restaurantName: String){
        val defaultImage = "https://firebasestorage.googleapis.com/v0/b/group7-acaa7.appspot.com/o/No_image_available.png?alt=media&token=9f69eae8-7c9c-4897-86f2-91a86d5b945d"
        val name = editItemName.text.toString()
        val price = editItemPrice.text.toString()

        val newItem = hashMapOf(
            "name" to name,
            "price" to price,
            "imageURL" to defaultImage
        )
        Log.d("!!!","RN : $restaurantName")
        db.collection(restaurantName)
            .add(newItem)
            .addOnSuccessListener {
                Toast.makeText(this,"Added item successfully", Toast.LENGTH_SHORT).show()
                Log.d("!!!","Added item successfully")
            }
            .addOnFailureListener {
                Toast.makeText(this,"Failed to add item",Toast.LENGTH_SHORT).show()
                Log.d("!!!","Failed to add item")
            }


    }

    fun updateItem(restaurantName : String?,itemName : String?){
        val updatedItem = hashMapOf(
            editItemName.text to "name",
            editItemPrice.text to "price"
        )
       db.collection(restaurantName.toString()).document()
           .update("name",updatedItem)




    }
    fun getRestaurant() : String?{
        val name = intent.getStringExtra(RESTAURANT_NAME)
        return name
    }
    fun getItemPosition ():String?{
        val position = intent.getStringExtra(ITEM_POSITION_NAME)
        return position
    }
}