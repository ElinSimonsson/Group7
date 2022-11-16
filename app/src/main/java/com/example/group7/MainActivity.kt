package com.example.group7

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlin.collections.ArrayList


class MainActivity : AppCompatActivity() {
    private lateinit var newRecyclerView: RecyclerView
    private lateinit var newArrayList: ArrayList<RestaurantsData>
    lateinit var imageId: Array<Int>
    lateinit var heading: Array<String>
    lateinit var address: Array<String>
    lateinit var userBtn: Button
    lateinit var auth: FirebaseAuth
    lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        db = Firebase.firestore
        auth = Firebase.auth
        //auth.signOut()

//        val user = auth.currentUser
//        if (user == null) {
//            signInAnonymously()
//        }

        imageId = arrayOf(
            R.drawable.roots,
            R.drawable.primo,
            R.drawable.asian,
            R.drawable.bankomat,
            R.drawable.chamsin,
            R.drawable.ilforno
        )

        heading = arrayOf(
            "Roots & Soil",
            "Primo Ciao Ciao",
            "Asian Kitchen",
            "BankOmat",
            "Chamsin",
            "IL Forno"
        )

        address = arrayOf(
            "Årstaängsvägen 21B, \n" +
                    "117 43 Stockholm",
            "Sjövikstorget 4, \n" +
                    "117 60 Stockholm",
            "Sjövikstorget 9, \n" +
                    "117 58 Stockholm",
            "Sjövikskajen 6, \n" +
                    "117 58 Stockholm",
            "Liljeholmsvägen 10, \n" +
                    "117 61 Stockholm",
            "Liljeholmstorget 5, \n" +
                    "117 63 Stockholm"
        )

        newRecyclerView = findViewById(R.id.restaurantRecyclerView)
        newRecyclerView.layoutManager = LinearLayoutManager(this)
        newRecyclerView.setHasFixedSize(true)

        newArrayList = arrayListOf()
        getRestaurantData()


        userBtn = findViewById<Button>(R.id.userBtn)
        signInBtn()

    }

    private fun signInAnonymously() {
        auth.signInAnonymously()
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Log.d("!!!", "signInAnonymously:success")
                } else {
                    Log.w("!!!", "signInAnonymously:failure", task.exception)
                }
            }
    }

    fun signInBtn() {
        if (auth.currentUser?.email == null) {
            userBtn.text = "Sign in"
            userBtn.setOnClickListener {
                val intent = Intent(this, UserActivity::class.java)
                startActivity(intent)
            }
        }
    }


    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        Log.d("!!!", "${currentUser?.isAnonymous}")
        //updateUI(currentUser)
    }

    override fun onResume() {
        super.onResume()
        val user = auth.currentUser

        if (user == null) {
            signInAnonymously()
            userBtn.text = "Log in"

            userBtn.setOnClickListener {
                val intent = Intent(this, UserActivity::class.java)
                startActivity(intent)
            }
        }
         else {
            if (!user.isAnonymous) {
                userBtn.text = "Profile"

                userBtn.setOnClickListener {
                    val intent = Intent(this, UserProfileActivity::class.java)
                    startActivity(intent)
                }
            } else {
                userBtn.text = "Log in"

                userBtn.setOnClickListener {
                    val intent = Intent(this, UserActivity::class.java)
                    startActivity(intent)
                }
            }
        }

        if (auth.currentUser?.email == "mcdonalds@admin.se") {
            val intentAdmin = Intent(this, AdminActivity::class.java)
            intentAdmin.putExtra(RES_MAIN, "Mcdonalds")
            startActivity(intentAdmin)
            finish()
        }
        if (auth.currentUser?.email == "asiankitchen@admin.se") {
            val intentAdmin = Intent(this, AdminActivity::class.java)
            intentAdmin.putExtra(RES_MAIN, "Asian Kitchen")
            startActivity(intentAdmin)
            finish()
        }
        if (auth.currentUser?.email == "rootssoil@admin.se") {
            val intentAdmin = Intent(this, AdminActivity::class.java)
            intentAdmin.putExtra(RES_MAIN, "Roots & Soil")
            startActivity(intentAdmin)
            finish()
        }
        if (auth.currentUser?.email == "primociaociao@admin.se") {
            val intentAdmin = Intent(this, AdminActivity::class.java)
            intentAdmin.putExtra(RES_MAIN, "Primo Ciao Ciao")
            startActivity(intentAdmin)
            finish()
        }
        if (auth.currentUser?.email == "bankomat@admin.se") {
            val intentAdmin = Intent(this, AdminActivity::class.java)
            intentAdmin.putExtra(RES_MAIN, "BankOmat")
            startActivity(intentAdmin)
            finish()
        }
        if (auth.currentUser?.email == "chamsin@admin.se") {
            val intentAdmin = Intent(this, AdminActivity::class.java)
            intentAdmin.putExtra(RES_MAIN, "Chamsin")
            startActivity(intentAdmin)
            finish()
        }
        if (auth.currentUser?.email == "ilforno@admin.se") {
            val intentAdmin = Intent(this, AdminActivity::class.java)
            intentAdmin.putExtra(RES_MAIN, "IL Forno")
            startActivity(intentAdmin)
            finish()
        }

    }

    private fun updateUI(user: FirebaseUser?) {

    }

    private fun getRestaurantData() {
        for (i in imageId.indices) {
            val restaurant = RestaurantsData(imageId[i], heading[i], address[i])
            newArrayList.add(restaurant)
        }

        val adapter = RestaurantAdapter(newArrayList)
        newRecyclerView.adapter = adapter

        adapter.setOnItemClickListener(object : RestaurantAdapter.onItemClickListener {

            override fun onItemClick(position: Int) {

                val intent = Intent(this@MainActivity, MenuActivity::class.java)
                intent.putExtra("restaurant", newArrayList[position].restaurantHeading)
                startActivity(intent)
            }
        }
        )
    }

    private operator fun Button.get(i: Int) {


    }
}





