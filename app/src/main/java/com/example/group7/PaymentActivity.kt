package com. example.group7

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract.Data
import android.text.InputFilter
import android.text.Spanned
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.lang.NumberFormatException


class PaymentActivity : AppCompatActivity() {
    lateinit var changeButton : TextView
    lateinit var titleWayToGetFoodTV : TextView
    lateinit var ccvText : EditText
    lateinit var monthText : EditText
    lateinit var yearText : EditText
    lateinit var postText : EditText
    lateinit var nameText : EditText
    lateinit var addressText : EditText
    lateinit var cityText : EditText
    lateinit var cardNumber : EditText
    lateinit var phoneNumberET : EditText
    lateinit var db : FirebaseFirestore
    lateinit var auth : FirebaseAuth
    var user: FirebaseUser? = null
    var userDeliveryChoice = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment)
        db = Firebase.firestore
        auth = Firebase.auth

        ccvText = findViewById(R.id.edit3number)
        monthText = findViewById(R.id.editMonthNumber)
        yearText = findViewById(R.id.editYearNumber)
        postText = findViewById(R.id.editPostNumber)
        nameText = findViewById(R.id.editNameText)
        addressText = findViewById(R.id.editAdressText)
        cityText = findViewById(R.id.editCityText)
        cardNumber = findViewById(R.id.editCardNumber)
        phoneNumberET = findViewById(R.id.editPhoneNumberText)
        changeButton = findViewById(R.id.changeButton)
        titleWayToGetFoodTV = findViewById(R.id.titleWayToGetFoodTV)
        val payBtn2: Button = findViewById(R.id.payBtn2)


        showTakeawayOrDeliveryWindow()

        var restaurantName = intent.getStringExtra(RES_NAME_PAYMENT)
        if (restaurantName == null) {
            restaurantName = "No restaurant name"
        }

        val actionBar: ActionBar? = supportActionBar
        actionBar?.title = restaurantName
        actionBar?.setDisplayHomeAsUpEnabled(true)

        changeButton.setOnClickListener {
            showTakeawayOrDeliveryWindow()
        }

        ccvText.filters = arrayOf<InputFilter>(MinMaxFilter(0, 999))
        monthText.filters = arrayOf<InputFilter>(MinMaxFilter(0, 12))
        yearText.filters = arrayOf<InputFilter>(MinMaxFilter(0, 99))
        postText.filters = arrayOf<InputFilter>(MinMaxFilter(0, 99999))



         user = auth.currentUser
        if (user?.email != null) {

            displayUserDataIfSavedInFireStore()


            payBtn2.setOnClickListener {

                val user_msg_error: String = ccvText.text.toString()

                if (user_msg_error.trim().isEmpty()) {
                    ccvText.error = "Required"
                    Toast.makeText(
                        applicationContext,
                        getString(R.string.ccvRequired_textview),
                        Toast.LENGTH_SHORT
                    ).show()
                } else if (yearText.text.toString().trim().isEmpty()) {
                    yearText.error = "Required"
                    Toast.makeText(
                        applicationContext,
                        getString(R.string.yearRequired_textview),
                        Toast.LENGTH_SHORT
                    ).show()
                } else if (monthText.text.toString().trim().isEmpty()) {
                    monthText.error = "Required"
                    Toast.makeText(
                        applicationContext,
                        getString(R.string.monthRequired_textview),
                        Toast.LENGTH_SHORT
                    ).show()
                } else if (cardNumber.text.toString().trim().isEmpty()) {
                    cardNumber.error = "Required"
                    Toast.makeText(
                        applicationContext,
                        getString(R.string.cardNumberRequired_textview),
                        Toast.LENGTH_SHORT
                    )
                        .show()
                } else if (postText.text.toString().trim().isEmpty()) {
                    postText.error = "Required"
                    Toast.makeText(
                        applicationContext,
                        getString(R.string.postCodeRequired_textview),
                        Toast.LENGTH_SHORT
                    )
                        .show()
                } else if (cityText.text.toString().trim().isEmpty()) {
                    cityText.error = "Required"
                    Toast.makeText(
                        applicationContext,
                        getString(R.string.cityRequired_textview),
                        Toast.LENGTH_SHORT
                    ).show()
                } else if (addressText.text.toString().trim().isEmpty()) {
                    addressText.error = "Required"
                    Toast.makeText(
                        applicationContext,
                        getString(R.string.addressRequired_textview),
                        Toast.LENGTH_SHORT
                    ).show()
                } else if (nameText.text.toString().trim().isEmpty()) {
                    nameText.error = "Required"
                    Toast.makeText(
                        applicationContext,
                        getString(R.string.nameRequired_textview),
                        Toast.LENGTH_SHORT
                    ).show()
                } else {

                    saveItemsAndUserDataFireStore()

                    val intent = Intent(this, SuccessPaymentActivity::class.java)
                    intent.putExtra(RES_NAME_SUCCESS_PAYMENT, restaurantName)
                    intent.putExtra(USER_DELIVERY_CHOICE, userDeliveryChoice)
                    startActivity(intent)

                }
            }
        }
    }


    override fun onOptionsItemSelected(item: android.view.MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()
        }
        return super.onOptionsItemSelected(item)
    }

    fun showTakeawayOrDeliveryWindow() {
        val dialogBinding = layoutInflater.inflate(R.layout.question_window, null)
        val dialog = Dialog(this)
        dialog.setContentView(dialogBinding)
        dialog.setCancelable(false)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.show()

        val deliveryButton = dialogBinding.findViewById<TextView>(R.id.delivery_button)
        val takeawayButton = dialogBinding.findViewById<TextView>(R.id.takeaway_button)

        deliveryButton.setOnClickListener {
            userDeliveryChoice = "delivery"
            titleWayToGetFoodTV.text = getString(R.string.homeDelivery_textview)
            dialog.dismiss()
        }

        takeawayButton.setOnClickListener {
            userDeliveryChoice = "takeaway"
            titleWayToGetFoodTV.text = getString(R.string.takeaway_textview)
            dialog.dismiss()
        }
    }

    fun displayUserDataIfSavedInFireStore () {
        user = auth.currentUser

            val docRef = db.collection("users")
                .document(user!!.uid)
            docRef.get()
                .addOnSuccessListener { document ->
                    if (document != null) {
                        val name = document.data?.get("name")
                        val address = document.data?.get("address")
                        val phoneNumber = document.data?.get("phoneNumber")
                        val city = document.data?.get("city")
                        val postCode = document.data?.get("postCode")

                        if (name != null) {
                            nameText.setText(name.toString())
                        }
                        if (address != null) {
                            addressText.setText(address.toString())
                        }
                        if (phoneNumber != null) {
                            phoneNumberET.setText(phoneNumber.toString())
                        }
                        if (city != null) {
                            cityText.setText(city.toString())
                        }
                        if (postCode != null) {
                            postText.setText(postCode.toString())
                        }
                    }
                }
        }


    fun saveItemsAndUserDataFireStore () {
        var restaurantName = intent.getStringExtra(RES_NAME_PAYMENT)
        if (restaurantName == null) {
            restaurantName = "No restaurant name"
        }

        user = auth.currentUser
        val userData = hashMapOf(
            "postText" to postText.text.toString(),
            "name" to nameText.text.toString(),
            "address" to addressText.text.toString(),
            "phoneNumber" to phoneNumberET.text.toString(),
            "cityText" to cityText.text.toString(),
            "user" to user?.uid,
            "userChoice" to userDeliveryChoice
        )
        Log.d("!!!", "city : ${cityText.text}")

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

                            Log.d("!!!", "Added order successfully")
                            DataManager.itemInCartList.clear()
                        }
                        .addOnFailureListener {
                            Toast.makeText(
                                this,
                                "Failed to add order",
                                Toast.LENGTH_SHORT
                            )
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
                        Log.d("!!!", "Added order successfully")
                    }
                    .addOnFailureListener {
                        Log.d("!!!", "failed to upload data")
                    }
            }
    }

    inner class MinMaxFilter() : InputFilter {
        private var intMin: Int = 0
        private var intMax: Int = 0

        constructor(minValue: Int, maxValue: Int) : this() {
            this.intMin = minValue
            this.intMax = maxValue
        }

        override fun filter(
            source: CharSequence,
            start: Int,
            end: Int,
            dest: Spanned,
            dStart: Int,
            dEnd: Int
        ): CharSequence? {
            try {
                val input = Integer.parseInt(dest.toString() + source.toString())
                if (isInRange(intMin, intMax, input)) {
                    return null
                }
            } catch (e: NumberFormatException) {
                e.printStackTrace()
            }
            return ""
        }

        private fun isInRange(a: Int, b: Int, c: Int): Boolean {
            return if (b > a) c in a..b else c in b..a
        }
    }
}
