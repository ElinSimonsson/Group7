package com.example.group7

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
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
        listOfItem = mutableListOf()

        db = Firebase.firestore

        modeOfDeliveryTV = findViewById(R.id.modeOfDeliveryTextView)
        customerTV = findViewById(R.id.customerTextView)
        customerNameTV = findViewById(R.id.customerNameTextView)
        addressTV = findViewById(R.id.addressTextView)
        postAndCityTextView = findViewById(R.id.postAndCityTextView)
        phoneNumberTV = findViewById(R.id.phoneNumberTextView)
        deliveryEditText = findViewById(R.id.estimatedDeliveryET)
        estimatedDeliveryTV = findViewById(R.id.estimatedDeliveryTV)


        val receviedButton = findViewById<Button>(R.id.orderReceviedbutton)
        receviedButton.setOnClickListener {

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
            val linearLayoutManager: LinearLayoutManager = LinearLayoutManager(this)

            recyclerView.layoutManager = linearLayoutManager
            recyclerView.adapter = DetailedOrderRecycleAdapter(it)
        }
        fetchOrderDataTest()
    }

    fun getDocumentId(): String? = intent.getStringExtra("documentId")

    fun getRestaurantName(): String? = intent.getStringExtra("restaurant")

    fun getCustomerData(restaurant: String, documentId: String) {
        db.collection("Order").document(restaurant)
            .collection("userOrders").document(documentId)
            .collection("userData")
            .get().addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // val list = mutableListOf<Customer>()
                    for (document in task.result) {
                        val userChoice = document.data["userChoice"].toString()
                        when (userChoice) {
                            "takeaway" -> {
                                addressTV.visibility = View.GONE
                                postAndCityTextView.visibility = View.GONE
                                modeOfDeliveryTV.text = "Avhämtning"
                                estimatedDeliveryTV.text = "Maten beräknas vara klar för upphämtning om:"
                            }
                            "delivery" -> {
                                modeOfDeliveryTV.text = "Hemleverans"
                                estimatedDeliveryTV.text = "Beräknad leverans om: "
                            }
                        }
                        val name = document.data["name"].toString()
                        customerNameTV.text = "Namn: " + name
                        val address = document.data["address"].toString()
                        addressTV.text = "Adress: " + address
                        val postText = document.data["postText"].toString()
                        val city = document.data["cityText"].toString()
                        Log.d("!!!", "stad: $city, postnummer: $postText")
                        postAndCityTextView.text = "$postText, $city"
                        val phoneNumber = document.data["phoneNumber"]
                        phoneNumberTV.text = "Mobilnummer: " + phoneNumber.toString()
                        userId = document.data["user"].toString()

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

    fun fetchOrderDataTest() {
        val documentID = getDocumentId()
        val restaurant = getRestaurantName()
        db.collection("Orders").document(restaurant!!).collection("userOrders")
            .document(documentID!!).collection("item")
            .get().addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    var total = 0
                    for (document in task.result) {
                        val name = document.data["name"].toString()
                        val price = document.data["price"].toString().toInt()
                        val amount = document.data["amount"].toString().toInt()
                        val pris = price * amount
                        total += pris
                        val orderItem = OrderData(name, amount, price)
                        listOfItem.add(orderItem)
                    }
                    Log.d("!!!", "totalPris är: $total")
                }
            }
    }

    fun sendDeliveryDataToFirebase () {
        val restaurant = getRestaurantName()

        val deliveryTime = hashMapOf(
            "deliveryTime" to deliveryEditText.text.toString()
        )
        Log.d("!!!", "delivery: $deliveryTime")
        if(restaurant!= null) {
        db.collection(RESTAURANT_STRING)
            .document(restaurant)
            .collection("userDelivery")
            .document(userId)
            .set(deliveryTime)
            .addOnSuccessListener {
                Log.d("!!!", "delivery lyckades")
            }
            .addOnFailureListener {
                Log.d("!!!", "fail att sända data till firebase")
            }
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

    fun getTotalPrice(listOfOrderItems: MutableList<OrderData>): Int {
        var total = 0
        for (item in listOfOrderItems) {
            var itemPrice = item.price?.times(item.amount!!)
            total = total + itemPrice!!
        }
        return total
    }
}

