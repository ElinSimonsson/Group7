package com.example.group7

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class orderActivity : AppCompatActivity() {

    lateinit var saveButton: Button
    lateinit var nameEditText: EditText
    lateinit var priceEditText: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order)

        saveButton = findViewById(R.id.button)
        nameEditText = findViewById(R.id.nameEditText)
        priceEditText = findViewById(R.id.priceEditText)

        saveButton.setOnClickListener{
            val name = nameEditText.text.toString()
            val price = priceEditText.text.toString().toInt()

            addOrderFirestore(name,price)
        }

    }

    fun addOrderFirestore(name : String, price : Int ){
        val db = FirebaseFirestore.getInstance()
        val order : MutableMap<String , Any> = HashMap()
        order["name"] = name
        order["price"] = price

        db.collection("Orders")
            .add(order)
            .addOnSuccessListener {
                Toast.makeText(this,"Successfully",Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener{
                Toast.makeText(this,"Fail",Toast.LENGTH_SHORT).show()
            }
    }
}