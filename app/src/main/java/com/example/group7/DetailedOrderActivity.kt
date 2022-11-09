package com.example.group7

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.ActionBar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class DetailedOrderActivity : AppCompatActivity() {
    lateinit var customerTV: TextView
    lateinit var customerNameTV: TextView
    lateinit var postAndCityTextView: TextView
    lateinit var addressTV: TextView
    lateinit var phoneNumberTV: TextView
    lateinit var itemsTextView: TextView
    lateinit var estimatedDeliveryTV : TextView
    lateinit var modeOfDeliveryTV : TextView
    lateinit var deliveryEditText: EditText
    lateinit var db: FirebaseFirestore
    lateinit var recyclerView: RecyclerView
    lateinit var listOfItem: MutableList<OrderData>
    var userId = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detailed_order)

        modeOfDeliveryTV = findViewById(R.id.modeOfDeliveryTextView)
        customerTV = findViewById(R.id.customerTextView)
        customerNameTV = findViewById(R.id.customerNameTextView)
        addressTV = findViewById(R.id.addressTextView)
        postAndCityTextView = findViewById(R.id.postAndCityTextView)
        phoneNumberTV = findViewById(R.id.phoneNumberTextView)
        itemsTextView = findViewById(R.id.itemsTextView)
        deliveryEditText = findViewById(R.id.estimatedDeliveryET)
        estimatedDeliveryTV = findViewById(R.id.estimatedDeliveryTV)
        listOfItem = mutableListOf()
        db = Firebase.firestore

        val actionBar: ActionBar? = supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)

        val receivedButton = findViewById<Button>(R.id.orderReceviedbutton)
        receivedButton.setOnClickListener {

            if (deliveryEditText.text.toString().trim().isEmpty()) {
                deliveryEditText.error = "Required"
            } else {
                sendDeliveryDataToFirebase()
                deleteOrder()
                finish()
            }
        }

        val documentID = getDocumentId()
        val restaurant = getRestaurantName()
        if (restaurant != null) {
            if (documentID != null) {
                getCustomerData(restaurant, documentID)
            }
        }

        fetchOrderData {
            recyclerView = findViewById(R.id.detailedOrderRecyclerView)
            val linearLayoutManager = LinearLayoutManager(this)
            recyclerView.layoutManager = linearLayoutManager
            recyclerView.adapter = DetailedOrderRecycleAdapter(it)
        }
    }

    fun getDocumentId(): String? = intent.getStringExtra("documentId")

    fun getRestaurantName(): String? = intent.getStringExtra("restaurant")

    fun getCustomerData(restaurant: String, documentId: String) {
        db.collection("Order").document(restaurant)
            .collection("userOrders").document(documentId)
            .collection("userData")
            .get().addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    for (document in task.result) {
                        userId = document.data["user"].toString()
                        val name = document.data["name"].toString()
                        val address = document.data["address"].toString()
                        val postText = document.data["postText"].toString()
                        val city = document.data["cityText"].toString()
                        val phoneNumber = document.data["phoneNumber"].toString()
                        val userChoice = document.data["userChoice"].toString()

                        customerNameTV.text = "Namn: " + name
                        addressTV.text = "Adress: " + address
                        postAndCityTextView.text = "$postText, $city"
                        phoneNumberTV.text = "Mobilnummer: " + phoneNumber

                        when (userChoice) {
                            "takeaway" -> initializeTakeawayLayout()
                            "delivery" -> initializeDeliveryLayout()
                        }
                    }
                }
            }
    }

    fun fetchOrderData(myCallback: (MutableList<OrderData>) -> Unit) {
        val documentID = getDocumentId()
        val restaurant = getRestaurantName()
        db.collection("Order").document(restaurant!!).collection("userOrders")
            .document(documentID!!).collection("Items")
            .get().addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val list = mutableListOf<OrderData>()
                    for (document in task.result) {
                        val name = document.data["name"].toString()
                        val price = document.data["price"].toString().toInt()
                        val amount = document.data["amount"].toString().toInt()
                        val orderItem = OrderData(name, amount, price)
                        list.add(orderItem)
                    }
                    myCallback(list)
                }
            }
    }

    fun sendDeliveryDataToFirebase () {
        val restaurant = getRestaurantName()

        val deliveryTime = hashMapOf(
            "deliveryTime" to deliveryEditText.text.toString()
        )
        if(restaurant!= null) {
        db.collection(RESTAURANT_STRING)
            .document(restaurant)
            .collection("userDelivery")
            .document(userId)
            .set(deliveryTime)
        }
    }

    fun deleteOrder() {
        db.collection("Order")
            .document(getRestaurantName()!!)
            .collection("userOrders")
            .document(getDocumentId()!!)
            .delete()
            .addOnSuccessListener {
                Log.d("!!!", "item deleted")
            }
            .addOnFailureListener {
                Log.d("!!!", "item not deleted : $it")
            }
    }

    fun initializeDeliveryLayout () {
        modeOfDeliveryTV.visibility = View.VISIBLE
        customerTV.visibility = View.VISIBLE
        customerNameTV.visibility = View.VISIBLE
        phoneNumberTV.visibility = View.VISIBLE
        addressTV.visibility = View.VISIBLE
        postAndCityTextView.visibility = View.VISIBLE
        itemsTextView.visibility = View.VISIBLE
        modeOfDeliveryTV.text = "Hemleverans"
        estimatedDeliveryTV.text = "Beräknad leverans om: "
    }

    fun initializeTakeawayLayout () {
        modeOfDeliveryTV.visibility = View.VISIBLE
        customerTV.visibility = View.VISIBLE
        customerNameTV.visibility = View.VISIBLE
        phoneNumberTV.visibility = View.VISIBLE
        itemsTextView.visibility = View.VISIBLE
        modeOfDeliveryTV.text = "Avhämtning"
        estimatedDeliveryTV.text = "Maten beräknas vara klar för upphämtning om:"
    }

    override fun onOptionsItemSelected(item: android.view.MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()
        }
        return super.onOptionsItemSelected(item)
    }
}

