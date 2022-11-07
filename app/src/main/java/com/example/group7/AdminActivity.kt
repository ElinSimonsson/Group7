package com.example.group7

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore


class AdminActivity : AppCompatActivity() {

    lateinit var recyclerView: RecyclerView
    lateinit var db: FirebaseFirestore
    lateinit var adminMatTextView: TextView
    lateinit var adminDryckTextView: TextView
    lateinit var orderButton: TextView
    lateinit var menuButton: TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin)

        menuButton = findViewById(R.id.menuTV)
        orderButton = findViewById(R.id.orderTV)

        menuButton.setOnClickListener {
            replaceWithMenuFragment()
            Log.d("!!!", "menu button körs")
        }
        orderButton.setOnClickListener {
            replaceWithOrderFragment()
        }


//        val orderButton = findViewById<Button>(R.id.orderButton)
//
//        orderButton.setOnClickListener {
//            val intent = Intent(this, OrderActivity::class.java)
//            startActivity(intent)
//        }
//
//        db = Firebase.firestore
//        val restaurantName = getRestaurantName()
//
//        val fab = findViewById<FloatingActionButton>(R.id.floatingActionButton)
//        fab.setOnClickListener {
//            val intentFab = Intent(this,AdminDisplayItem_Activity::class.java)
//            intentFab.putExtra("newUser" , 1)
//            intentFab.putExtra("restaurantNameFAB",restaurantName)
//            startActivity(intentFab)
//        }
//        adminMatTextView = findViewById(R.id.adminMatTextView)
//        adminDryckTextView = findViewById(R.id.adminDryckTextView)
//
//        startMenuFragment()
//
//        adminMatTextView.setOnClickListener {
//            startMenuFragment()
//        }
//        adminDryckTextView.setOnClickListener {
//            startDrinkFragment()
//        }


//        val docRef = db.collection("Orders").document("Mcdonalds")
//            .collection("Orders")
//
//        docRef.addSnapshotListener { snapshot, e ->
//
//            if (snapshot != null) {
//                Log.d("!!!", "item updated!")
//                for(document in snapshot.documents) {
//                    val documentId = document.id
//                    Log.d("!!!", "Testar att skriva ut ID: $documentId")
//
//                    val docref1 = db.collection("Orders").document("Mcdonalds")
//                        .collection("Orders").document(documentId).collection("Order")
//
//                    val orderList = mutableListOf<Order>()
//
//                    docref1.addSnapshotListener { snapshot1, _ ->
//                        if(snapshot1 != null) {
//                            orderList.clear()
//                            for(item in snapshot1.documents) {
//                                val order = item.toObject<Order>()
//                                if(order!= null) {
//                                    orderList.add(order)
//                                }
//                            }
//                            for(item in orderList) {
//                                Log.d("!!!", "$item")
//                            }
//                        }
//
//                    }
//                }
//            }
//        }


//        val name = hashMapOf(
//            "name" to "Mcdonalds"
//        )
//
//        db.collection("Orders").document("Mcdonalds")
//            .collection("Orders")
//            .add(name)
//            .addOnSuccessListener { documentReference ->
//                Log.d("!!!", "DocumentSnapshot written with ID: ${documentReference.id}")
//
//
//                val order = hashMapOf(
//                    "name" to "Planksteak",
//                    "amount" to 2,
//                    "price" to 100
//                )
//                db.collection("Orders").document("Mcdonalds")
//                    .collection("Orders").document(documentReference.id).collection("Order")
//                    .add(order)
//                    .addOnSuccessListener { Log.d("!!!", "DocumentSnapshot successfully written!") }
//                    .addOnFailureListener { e -> Log.w("!!!", "Error writing document", e) }
//            }
//            .addOnFailureListener { e ->
//                Log.w("!!!", "Error adding document", e)
//            }

        replaceWithMenuFragment()

    }

    private fun replaceWithOrderFragment() {
        val orderFragment = OrderFragment()
        val bundle = Bundle()
        bundle.putString("restaurant", getRestaurantName())
        orderFragment.arguments = bundle
        val fragmentManager = supportFragmentManager
        val transaction = fragmentManager.beginTransaction()
        transaction.replace(R.id.adminContainer, orderFragment)
        transaction.commit()
    }

    private fun replaceWithMenuFragment() {
        val menuFragment = AdminMenuFragment()
        Log.d("!!!", "funktion replace körs")
        val bundle = Bundle()
        bundle.putString("restaurant", getRestaurantName())
        menuFragment.arguments = bundle
        val fragmentManager = supportFragmentManager
        val transaction = fragmentManager.beginTransaction()
        transaction.replace(R.id.adminContainer, menuFragment)
        transaction.commit()
    }

    private fun startDrinkFragment() {
        val AdminFragment = AdminDrinkFragment()
        val bundle = Bundle()
        bundle.putString(RES_NAME_DRINK_FRAGMENT, getRestaurantName())
        AdminFragment.arguments = bundle
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.menuContainer, AdminFragment)
        fragmentTransaction.commit()
    }

    private fun startMenuFragment() {
        val AdminFragment = AdminFoodFragment()
        val bundle = Bundle()
        bundle.putString(RES_NAME_MENU_FRAGMENT, getRestaurantName())
        AdminFragment.arguments = bundle
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.menuContainer, AdminFragment)
        fragmentTransaction.commit()
    }


    fun getRestaurantName(): String {
        val restaurantName = intent.getStringExtra(RES_MAIN).toString()
        Log.d("!!!", "rname fun admin : $restaurantName")
        return restaurantName
    }
}