package com.example.group7

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.ActionBar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.text.SimpleDateFormat
import java.util.*

class SuccessPaymentActivity : AppCompatActivity() {
    lateinit var waitConfirmationTextView : TextView
    lateinit var modeOfDeliveryTextView : TextView
    lateinit var estimatedMinutesTextView : TextView
    lateinit var okButton : TextView
    lateinit var estimatedDeliveryTimeTV : TextView
    lateinit var orderIsConfirmedTV : TextView
    lateinit var smallProgressBar : ProgressBar
    lateinit var largeProgressBar: ProgressBar
    lateinit var db : FirebaseFirestore
    lateinit var auth : FirebaseAuth
    var restaurant = ""
    var user : FirebaseUser? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_success_payment)
        waitConfirmationTextView = findViewById(R.id.waitForConfirmationTV)
        modeOfDeliveryTextView = findViewById(R.id.modeOfDeliveryTV)
        estimatedMinutesTextView = findViewById(R.id.deliveryMinutesTV)
        estimatedDeliveryTimeTV = findViewById(R.id.estimatedDeliveryTimeTV)
        orderIsConfirmedTV = findViewById(R.id.orderIsConfirmedTV)
        okButton = findViewById(R.id.okButton)
        smallProgressBar = findViewById(R.id.smallProgressBar)
        largeProgressBar = findViewById(R.id.largeProgressBar)

        db = Firebase.firestore
        auth = Firebase.auth
        user = auth.currentUser

        restaurant = intent.getStringExtra(RES_NAME_SUCCESS_PAYMENT).toString()
        val actionBar: ActionBar? = supportActionBar
        actionBar?.title = restaurant

        snapShotListenerDelivery()
        okButton.setOnClickListener {
            deleteDeliveryTimeInFireStore()
            }
    }

    override fun onStop() {
        super.onStop()
        deleteDeliveryTimeInFireStore()
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
                    return@addSnapshotListener
                }
                if(snapshot != null && snapshot.exists()) {
                    val minutes = snapshot.data?.get("deliveryTime").toString().toInt()
                    estimatedMinutesTextView.text = getString(R.string.deliveryMinutes_textview, minutes)
                    val estimatedDeliveryTime = getEstimatedDeliveryTime(minutes)
                    estimatedDeliveryTimeTV.text = getString(R.string.estimatedDeliveryTime_textview, estimatedDeliveryTime)
                    setLayoutConfirmedOrder()
                } else {
                    Log.d("!!!", "nuvarande data : null")
                }
            }
        }
    }

    fun getEstimatedDeliveryTime (minutes: Int): String {
        val currentTime = Calendar.getInstance()
        currentTime.add(Calendar.MINUTE, minutes)
        val simpleTimeFormat = SimpleDateFormat("yyyy-MM-dd 'kl' HH:mm", Locale.getDefault())
        val estimatedDeliveryTime = simpleTimeFormat.format(currentTime.time)
        return estimatedDeliveryTime
    }

    fun setLayoutConfirmedOrder () {
        val userDeliveryChoice = intent.getStringExtra(USER_DELIVERY_CHOICE)
        when (userDeliveryChoice) {
            "delivery" -> modeOfDeliveryTextView.text = getString(R.string.homeDelivery_textview)
            "takeaway" -> modeOfDeliveryTextView.text = getString(R.string.takeaway_textview)
        }

        waitConfirmationTextView.visibility = View.GONE
        smallProgressBar.visibility = View.GONE
        largeProgressBar.visibility = View.GONE
        orderIsConfirmedTV.visibility = View.VISIBLE
        modeOfDeliveryTextView.visibility = View.VISIBLE
        estimatedMinutesTextView.visibility = View.VISIBLE
        estimatedDeliveryTimeTV.visibility = View.VISIBLE
        okButton.visibility = View.VISIBLE
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