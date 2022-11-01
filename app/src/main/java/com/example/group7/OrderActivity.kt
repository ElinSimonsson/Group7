package com.example.group7

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.firestore.FirebaseFirestore

class OrderActivity : AppCompatActivity() {

    lateinit var saveButton: Button
    lateinit var nameEditText: EditText
    lateinit var priceEditText: EditText
    lateinit var payBtn : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order)

        saveButton = findViewById(R.id.button)
        nameEditText = findViewById(R.id.nameEditText)
        priceEditText = findViewById(R.id.priceEditText)
        payBtn = findViewById(R.id.payBtn)

        saveButton.setOnClickListener{
            val name = nameEditText.text.toString()
            val price = priceEditText.text.toString().toInt()

            addOrderFirestore(name,price)
        }
        payBtn.setOnClickListener {
            val intent = Intent(this, PaymentActivity::class.java)
            startActivity(intent)

        }
    }


    fun addOrderFirestore(name : String, price : Int ){
        val db = FirebaseFirestore.getInstance()
        val order = hashMapOf(
            "name" to name,
            "price" to price
        )

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