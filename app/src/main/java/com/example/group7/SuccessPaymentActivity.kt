package com.example.group7

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.ActionBar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class SuccessPaymentActivity : AppCompatActivity() {
    lateinit var waitConfirmationTextView : TextView
    lateinit var modeOfDeliveryTextView : TextView
    lateinit var messageDeliveryTimeTextView : TextView
    lateinit var okButton : TextView
    lateinit var db : FirebaseFirestore
    lateinit var auth : FirebaseAuth
    var restaurant = ""
    var user : FirebaseUser? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_success_payment)
        waitConfirmationTextView = findViewById(R.id.waitForConfirmationTV)
        modeOfDeliveryTextView = findViewById(R.id.modeOfDeliveryTV)
        messageDeliveryTimeTextView = findViewById(R.id.deliveryTimeMessageTV)
        okButton = findViewById(R.id.okButton)

        db = Firebase.firestore
        auth = Firebase.auth
        user = auth.currentUser

        restaurant = intent.getStringExtra(RES_NAME_SUCCESS_PAYMENT).toString()
        val actionBar: ActionBar? = supportActionBar
        actionBar?.title = restaurant
        actionBar?.setDisplayHomeAsUpEnabled(true)

        snapShotListenerDelivery()
        okButton.setOnClickListener {
            deleteDeliveryTimeInFireStore()
            }
    }

    fun snapShotListenerDelivery () {
        if(user!= null) {
            Log.d("!!!", "nuvarande userId: ${user!!.uid}")
           val docRef = db.collection("restaurants")
               .document(restaurant)
               .collection("userDelivery")
               .document(user!!.uid)

            docRef.addSnapshotListener { snapshot, e ->
                if(e != null) {
                    Log.d("!!!", "Listen failed")
                }
                if(snapshot != null && snapshot.exists()) {
                    val delivery = snapshot.data?.get("deliveryTime").toString().toInt()
                    messageDeliveryTimeTextView.text = "om ca $delivery min"
                    var userDeliveryChoice = intent.getStringExtra(USER_DELIVERY_CHOICE)
                    when (userDeliveryChoice) {
                        "delivery" -> modeOfDeliveryTextView.text = "Hemleverans"
                        "takeaway" -> modeOfDeliveryTextView.text = "Avhämtning"
                    }
                    waitConfirmationTextView.visibility = View.GONE
                    modeOfDeliveryTextView.visibility = View.VISIBLE
                    messageDeliveryTimeTextView.visibility = View.VISIBLE
                    okButton.visibility = View.VISIBLE
                } else {
                    Log.d("!!!", "nuvarande data : null")
                }
            }
        }
    }

    fun deleteDeliveryTimeInFireStore () {
        if(user!= null) {
            db.collection("restaurants")
                .document(restaurant)
                .collection("userDelivery")
                .document(user!!.uid)
                .delete()
                .addOnSuccessListener {
                    Log.d("!!!", "item deleted")
                }
                .addOnFailureListener {
                    Log.d("!!!", "item not deleted : $it")
                }
        }
    }
}