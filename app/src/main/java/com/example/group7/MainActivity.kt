package com.example.group7

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
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
    lateinit var distance: Array<String>
    lateinit var userBtn: Button
    lateinit var auth: FirebaseAuth
    lateinit var db: FirebaseFirestore
    lateinit var adressView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        db = Firebase.firestore
        auth = Firebase.auth
        //auth.signOut()

        val user = auth.currentUser
        if (user == null) {
            signInAnonymously()
        }

        imageId = arrayOf(
            R.drawable.roots,
            R.drawable.primo,
            R.drawable.asian,
            )

        heading = arrayOf(
            "Roots & Soil",
            "Primo Ciao Ciao",
            "Asian Kitchen",
            )

        distance = arrayOf(
            "Distans 120m",
            "Distans 400m",
            "Distans 520m",
            )

        newRecyclerView = findViewById(R.id.restaurantRecyclerView)
        newRecyclerView.layoutManager = LinearLayoutManager(this)
        newRecyclerView.setHasFixedSize(true)

        newArrayList = arrayListOf()
        getUserdata()

        adressView = findViewById(R.id.adressView)

        getUserAdress {
            adressView.text = it
        }

        userBtn = findViewById<Button>(R.id.userBtn)
        userBtn.setOnClickListener {
            val intent = Intent(this, UserActivity::class.java)
            startActivity(intent)
        }
    }

    private fun signInAnonymously() {
        auth.signInAnonymously()
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser?.email
                    Log.d("!!!", "signInAnonymously:success")
                    if(user == null) {
                        Log.d("!!!", "null")
                    } else {
                        Log.d("!!!", user)
                    }
                } else {
                    Log.w("!!!", "signInAnonymously:failure", task.exception)
//                    updateUI(null)
                }
            }
    }

//    private fun linkAccount(userId: String) {
//        val mail = "$userId@anonymous.se"
//        val credential = EmailAuthProvider.getCredential(mail, "123456789")
//        auth.currentUser!!.linkWithCredential(credential)
//            .addOnCompleteListener(this) { task ->
//                if (task.isSuccessful) {
//                    Log.d("!!!", "linkWithCredential:success")
//                    val user = task.result?.user
//                    //updateUI(user)
//                    val testUser = auth.currentUser?.email
//                    val anomys = auth.currentUser?.isAnonymous
//
//                } else {
//                    Log.w("!!!", "linkWithCredential:failure", task.exception)
//                    //updateUI(null)
//                }
//            }
//    }
//

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

       // Changes text of 'UserBtn' to everything before @ in the email
//        if (auth.currentUser != null) {
//
//                val userEmail = auth.currentUser!!.email.toString()
//                val nameInEmail = userEmail.substring(0, userEmail.indexOf('@'))
//            val mail = userEmail.substring(userEmail.indexOf('@'))
//            Log.d("!!!", "substring: $mail")
//               // userBtn.text = nameInEmail
//            }


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

        getUserAdress {
            adressView.text = it
        }
    }

    private fun updateUI(user: FirebaseUser?) {

    }

    private fun getUserdata() {
        for (i in imageId.indices) {
            val restaurant = RestaurantsData(imageId[i], heading[i], distance[i])
            newArrayList.add(restaurant)
        }

        val adapter = RestaurantAdapter(newArrayList)
        newRecyclerView.adapter = adapter

        adapter.setOnItemClickListener(object : RestaurantAdapter.onItemClickListener{

            override fun onItemClick(position: Int) {

                val intent = Intent(this@MainActivity, MenuActivity::class.java)
                intent.putExtra("restaurant", newArrayList[position].restaurantHeading)
                startActivity(intent)
            }
        }
        )
    }

    fun getUserAdress(myCallback: (String) -> Unit) {

        db.collection("users").document(auth.currentUser?.uid.toString()).collection("adress")
            .get().addOnCompleteListener { task ->

                var userAdress = ""
                if (task.isSuccessful) {
                    for (document in task.result) {
                        val adress = document.data["adress"].toString()
                        userAdress = adress
                    }
                    myCallback(userAdress)
                }
            }
    }


    private operator fun Button.get(i: Int) {


    }
}




