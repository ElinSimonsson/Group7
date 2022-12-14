package com.example.group7

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Switch
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import java.text.SimpleDateFormat
import java.util.*




class AdminDisplayItem_Activity : AppCompatActivity() {



    lateinit var editItemName : EditText
    lateinit var editItemPrice : EditText
    lateinit var editItemImage : ImageView
    lateinit var newImage : String
    lateinit var progressBar: ProgressBar
    lateinit var uploadingTextView: TextView
    lateinit var db : FirebaseFirestore



    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_display_item)

        editItemName = findViewById(R.id.editItemName)
        editItemPrice = findViewById(R.id.editItemPrice)
        editItemImage = findViewById(R.id.editImageView)

        progressBar = findViewById(R.id.progressBar)
        uploadingTextView = findViewById(R.id.uploadingTextView)

        val saveBtn = findViewById<Button>(R.id.saveBtn)
        val deleteBtn = findViewById<Button>(R.id.deleteBtn)
        val switch = findViewById<Switch>(R.id.switch1)
        db = Firebase.firestore



        //Skickar ett eget intent med restaurang namnet till FAB
        val fabNumber = intent.getIntExtra("newUser" ,0)
        Log.d("!!!","fabNr :$fabNumber ")
        val fabRestaurant = intent.getStringExtra("restaurantNameFAB")
        Log.d("!!!","fabRn : $fabRestaurant")



        //result launcher som f??r en bild ifr??n lokala telefonen och laddar upp den p?? filestoreStorage,
        //sedan h??mtar den URL ifr??n storage p?? den bilden och fyller newImage med den str??ngen. Som sedan skickas till NewImage
        //funktionen om anv??ndaren v??ljer att spara den nya varan.

        val resultLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK) {

                    val imageUri: Uri? = result.data?.data

                    val formatter = SimpleDateFormat("yyyy_MM_dd_HH_mm_ss", Locale.getDefault())
                    val now = Date()
                    val fileName = formatter.format(now)

                    val storage = FirebaseStorage.getInstance()
                    val storageRef = storage.reference
                    val newImageRef = storageRef.child("images/$fileName")

                    if (imageUri != null) {
                        val uploadTask = newImageRef.putFile(imageUri)
                        uploadTask.continueWithTask { task ->
                            if (!task.isSuccessful) {
                                task.exception?.let {
                                    throw it
                                }
                            }

                        newImageRef.downloadUrl
                    }.addOnCompleteListener { task->
                        if(task.isSuccessful){
                            progressBar.visibility = View.GONE
                            uploadingTextView.visibility = View.GONE
                            editItemImage.visibility = View.VISIBLE

                            newImage = task.result.toString()
                            editItemImage.setImageURI(imageUri)
                            Toast.makeText(this,"Image Uploaded",Toast.LENGTH_SHORT).show()
                            Log.d("!!!","url : $newImage")
                        }
                    }
                }
            }
        }


        switch.isVisible = false
        //add drink or food

        if(fabNumber == 1) {
            switch.isVisible = true
            var type = "menu"


            editItemImage.setOnClickListener {

                //startar telefonens "image/galleri" och startar en result launcher som inv??ntar en bild
                val intent = Intent()
                intent.type = "image/*"
                intent.action = Intent.ACTION_GET_CONTENT

                resultLauncher.launch(intent)

            }
            switch.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    switch.text= getString(R.string.drink_switch)
                    type = "drink"
                }
                else {
                    switch.text = getString(R.string.menu_switch)
                    type = "menu"
                }
            }


            saveBtn.setOnClickListener {
                if (fabRestaurant != null) {
                    newItem(fabRestaurant,type)
                    returnToAdmin(fabRestaurant)
                } else {
                    Log.d("!!!","No restaurant name")
                }
            }
        } else {
            displayItem()
            saveBtn.setOnClickListener {
                updateItem()
                returnToAdmin(getRestaurant().toString())
            }
            deleteBtn.setOnClickListener {
                deleteItem()
                returnToAdmin(getRestaurant().toString())
            }
        }
    }


    fun displayItem (){
        db.collection(RESTAURANT_STRING)
            .document(getRestaurant().toString())
            .collection(getType().toString())
            .document(getDocumentID().toString())
            .get()
            .addOnSuccessListener { document ->
                if (document != null){
                    editItemName.setText(document.data!!["name"].toString())
                    Log.d("!!!","name : ${editItemName.text}")
                    editItemPrice.setText(document.data!!["price"].toString())
                    Glide.with(this)
                        .load(document.data!!["imageURL"])
                        .into(editItemImage)
                }
            }
            .addOnFailureListener {
                finish()
            }

    }
    fun newItem(restaurantNameFAB : String,type : String){
        val name = editItemName.text.toString()
        val price = editItemPrice.text.toString()
        var imageURL = "https://firebasestorage.googleapis.com/v0/b/group7-acaa7.appspot.com/o/No_image_available.png?alt=media&token=9f69eae8-7c9c-4897-86f2-91a86d5b945d"

        if(newImage != null){
            imageURL = newImage
        }


        val newItem = hashMapOf(
            "name" to name,
            "price" to price,
            "imageURL" to imageURL
        )
        Log.d("!!!","url : $imageURL")
        db.collection(RESTAURANT_STRING)
            .document(restaurantNameFAB)
            .collection(type)
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
            .collection(getType().toString())
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
            .collection(getType().toString())
            .document(getDocumentID().toString())
            .delete()
            .addOnSuccessListener {
                Log.d("!!!","item deleted")
            }
            .addOnFailureListener {
                Log.d("!!!","item not deleted : $it")
            }
    }
    fun returnToAdmin(rName : String){
        val intentAdmin = Intent(this, AdminActivity::class.java)
        intentAdmin.putExtra(RES_MAIN,rName)
        startActivity(intentAdmin)
        finish()
    }
    fun getRestaurant() : String?{
        return intent.getStringExtra(RES_NAME_ADAPTER)
    }
    fun getDocumentID () : String?{
        return intent.getStringExtra(DOCUMENT_ID)
    }
    fun getType(): String? {
        val type = intent.getStringExtra(TYPE)
        Log.d("!!!","type : $type")
        return type
    }
}