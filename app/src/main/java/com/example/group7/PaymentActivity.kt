package com.example.group7

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputFilter
import android.text.Spanned
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.lang.NumberFormatException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap

class PaymentActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment)

        val ccvText = findViewById<EditText>(R.id.edit3number)
        val monthText = findViewById<EditText>(R.id.editMonthNumber)
        val yearText = findViewById<EditText>(R.id.editYearNumber)
        val postText = findViewById<EditText>(R.id.editPostNumber)
        val nameText = findViewById<EditText>(R.id.editNameText)
        val adressText = findViewById<EditText>(R.id.editAdressText)
        val cityText = findViewById<EditText>(R.id.editCityText)
        val cardNumber = findViewById<EditText>(R.id.editCardNumber)
        val phoneNumber = findViewById<EditText>(R.id.editPhoneNumberText)
        val backBtn: Button = findViewById(R.id.backBtn)
        val payBtn2: Button = findViewById(R.id.payBtn2)
        val db = Firebase.firestore
        val auth = Firebase.auth

        var restaurantName = intent.getStringExtra(RES_NAME_PAYMENT)
        if (restaurantName == null) {
            restaurantName = "No restaurant name"
        }

        ccvText.filters = arrayOf<InputFilter>(MinMaxFilter(0, 999))
        monthText.filters = arrayOf<InputFilter>(MinMaxFilter(0, 12))
        yearText.filters = arrayOf<InputFilter>(MinMaxFilter(0, 99))
        postText.filters = arrayOf<InputFilter>(MinMaxFilter(0, 99999))

        backBtn.setOnClickListener {
            val intent = Intent(this, ShoppingCartActivity::class.java)
            startActivity(intent)

        }


        payBtn2.setOnClickListener {
            val user_msg_error: String = ccvText.text.toString()

            if (user_msg_error.trim().isEmpty()) {
                ccvText.error = "Required"
                Toast.makeText(applicationContext, "Skriv in CCV/CVV!", Toast.LENGTH_SHORT).show()
            } else if (yearText.text.toString().trim().isEmpty()) {
                yearText.error = "Required"
                Toast.makeText(applicationContext, "Skriv in ett år!", Toast.LENGTH_SHORT).show()
            } else if (monthText.text.toString().trim().isEmpty()) {
                monthText.error = "Required"
                Toast.makeText(applicationContext, "Skriv in en månad!", Toast.LENGTH_SHORT).show()
            } else if (cardNumber.text.toString().trim().isEmpty()) {
                cardNumber.error = "Required"
                Toast.makeText(applicationContext, "Skriv in kortnummer!", Toast.LENGTH_SHORT)
                    .show()
            } else if (postText.text.toString().trim().isEmpty()) {
                postText.error = "Required"
                Toast.makeText(applicationContext, "Skriv in ett postnummer!", Toast.LENGTH_SHORT)
                    .show()
            } else if (cityText.text.toString().trim().isEmpty()) {
                cityText.error = "Required"
                Toast.makeText(applicationContext, "Skriv in en stad!", Toast.LENGTH_SHORT).show()
            } else if (adressText.text.toString().trim().isEmpty()) {
                adressText.error = "Required"
                Toast.makeText(applicationContext, "Skriv in en adress!", Toast.LENGTH_SHORT).show()
            } else if (nameText.text.toString().trim().isEmpty()) {
                nameText.error = "Required"
                Toast.makeText(applicationContext, "Skriv in namn", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(applicationContext, "Informationen bekräftades", Toast.LENGTH_SHORT)
                    .show()

                //User information from input to Map
                // val creditCardInfo =  "${cardNumber.text}"  + "${monthText.text}-" + "${yearText.text}-" + "${ccvText.text}-"
                val userData = hashMapOf(
                    //  "postText " to postText.text.toString(),
                    "name" to nameText.text.toString(),
                    "address" to adressText.text.toString(),
                    "phoneNumber" to phoneNumber.text.toString()
                    //"cityText " to cityText.text.toString(),
                    //"creditCardInfo" to creditCardInfo
                )


                val name = hashMapOf(
                    "name" to restaurantName
                )

                db.collection("Order")
                    .document(restaurantName)
                    .collection("userOrders")
                    .add(name)
                    .addOnSuccessListener { documentReference ->
                        for (items in DataManager.itemInCartList) {
                            val order = hashMapOf(
                                "name" to items?.name.toString(),
                                "amount" to items?.totalCart.toString().toInt(),
                                "price" to items?.price.toString().toInt()
                            )

                            db.collection("Order")
                                .document(restaurantName)
                                .collection("userOrders")
                                .document(documentReference.id)
                                .collection("Items")
                                .add(order)
                                .addOnSuccessListener {

                                    Toast.makeText(this, "Order confirmed", Toast.LENGTH_SHORT)
                                        .show()
                                    Log.d("!!!", "Added order successfully")
                                }
                                .addOnFailureListener {

                                    Toast.makeText(this, "Failed to add order", Toast.LENGTH_SHORT)
                                        .show()
                                    Log.d("!!!", "Failed to add item")
                                }
                        }
                        db.collection("Order")
                            .document(restaurantName)
                            .collection("userOrders")
                            .document(documentReference.id)
                            .collection("userData")
                            .add(userData)
                            .addOnSuccessListener {

                                Toast.makeText(this, "Order confirmed", Toast.LENGTH_SHORT)
                                    .show()
                                Log.d("!!!", "Added order successfully")

                            }

                            .addOnFailureListener {
                                Log.d("!!!", "failed to upload data")
                            }
                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)


                    }

            }


        }
    }

    inner class MinMaxFilter() : InputFilter{
    private var intMin: Int = 0
    private var intMax: Int = 0

    constructor(minValue: Int, maxValue: Int) : this() {
        this.intMin = minValue
        this.intMax = maxValue
    }
    override fun filter(source: CharSequence, start: Int, end: Int, dest: Spanned, dStart: Int, dEnd: Int): CharSequence?{
        try {
            val input = Integer.parseInt(dest.toString() + source.toString())
            if (isInRange(intMin, intMax, input)){
                return null
            }
        } catch (e: NumberFormatException){
            e.printStackTrace()
        }
        return ""
    }
    private fun isInRange(a: Int, b: Int, c: Int): Boolean{
        return if (b > a) c in a..b else c in b..a
    }
}
}