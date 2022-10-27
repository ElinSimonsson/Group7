package com.example.group7

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


const val ITEM_POSITION_NAME = "ITEM_POSITION_NAME"
const val RESTAURANT_NAME = "RestaurantName"
const val DOCUMENT_ID = "DocumentID"
const val RESTAURANT_STRING = "restaurants"
const val MENU = "menu"

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


        //Skickar ett eget intent med restaurang namnet till FAB
        val fabNumber = intent.getIntExtra("newUser" ,0)
        Log.d("!!!","fabNr :$fabNumber ")
        val fabRestaurant = intent.getStringExtra("restaurantNameFAB")
        Log.d("!!!","fabRn : $fabRestaurant")


        displayItem()

        if(fabNumber == 1){
            saveBtn.setOnClickListener {
                if (fabRestaurant != null) {
                    newItem(fabRestaurant)
                    returnToAdmin()
                }
                else{
                    Log.d("!!!","No restaurant name")
                }
            }
        }
        else{
            saveBtn.setOnClickListener {
                updateItem()
                returnToAdmin()
            }
            deleteBtn.setOnClickListener {
                deleteItem()
                returnToAdmin()
            }
        }
        

    }
    fun displayItem (){
        db.collection(RESTAURANT_STRING)
            .document(getRestaurant().toString())
            .collection(MENU)
            .whereEqualTo("name",getItemPositionName())
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
    fun newItem(restaurantNameFAB : String){
        val defaultImage = "https://firebasestorage.googleapis.com/v0/b/group7-acaa7.appspot.com/o/No_image_available.png?alt=media&token=9f69eae8-7c9c-4897-86f2-91a86d5b945d"
        val name = editItemName.text.toString()
        val price = editItemPrice.text.toString()

        val newItem = hashMapOf(
            "name" to name,
            "price" to price,
            "imageURL" to defaultImage
        )
        db.collection(RESTAURANT_STRING)
            .document(restaurantNameFAB)
            .collection(MENU)
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

    fun updateItem(){

              db.collection(RESTAURANT_STRING)
                  .document(getRestaurant().toString())
                  .collection(MENU)
                  .document(getDocumentID().toString())
                  .update("name",editItemName.text.toString(), "price",editItemPrice.text.toString())
                    .addOnSuccessListener {
                        Log.d("!!!","item name updated")
                    }
                    .addOnFailureListener {
                       Log.d("!!!","item name not updated : $it")
                    }

    }
    fun deleteItem(){
        db.collection(RESTAURANT_STRING)
            .document(getRestaurant().toString())
            .collection(MENU)
            .document(getDocumentID().toString())
            .delete()
            .addOnSuccessListener {
                Log.d("!!!","item deleted")
            }
            .addOnFailureListener {
                Log.d("!!!","item not deleted : $it")
            }
    }
    fun returnToAdmin(){
        val intentAdmin = Intent(this, AdminActivity::class.java)
        intentAdmin.putExtra(RESTAURANT,"Mcdonalds")
        startActivity(intentAdmin)
        finish()
    }
    fun getRestaurant() : String?{
        val name = intent.getStringExtra(RESTAURANT_NAME)
        Log.d("!!!","fun getres : $name")
        return name
    }
    fun getItemPositionName ():String?{
        val position = intent.getStringExtra(ITEM_POSITION_NAME)
        Log.d("!!!","fun itemposname : $position")
        return position
    }
    fun getDocumentID () : String?{
        val id = intent.getStringExtra(DOCUMENT_ID)
        Log.d("!!!","fun id : $id")
        return id
    }
}