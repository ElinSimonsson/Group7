package com.example.group7

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class UserProfileActivity : AppCompatActivity() {
    lateinit var nameEditText: EditText
    lateinit var phoneNumberEditText: EditText
    lateinit var addressEditText: EditText
    lateinit var postCodeEditText: EditText
    lateinit var cityEditText: EditText
    lateinit var db : FirebaseFirestore
    lateinit var auth: FirebaseAuth
    var name = ""
    var phoneNumber = ""
    var address = ""
    var postCode = ""
    var city = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_profile)

        nameEditText = findViewById(R.id.profileNameEditText)
        phoneNumberEditText = findViewById(R.id.profilePhoneNumberEditText)
        addressEditText = findViewById(R.id.profileAddressEditText)
        postCodeEditText = findViewById(R.id.profilePostCodeEditText)
        cityEditText = findViewById(R.id.profileCityEditText)
        db = Firebase.firestore
        auth = Firebase.auth

        val actionBar: ActionBar? = supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)

        displayCurrentUserData()


        val saveButton = findViewById<TextView>(R.id.profileSaveButton).setOnClickListener{
            saveNewUserData()
            finish()
        }

        val logOutButton = findViewById<TextView>(R.id.logOutButton).setOnClickListener {
            auth.signOut()
            finish()
        }
    }

    fun saveNewUserData () {

        val userData = hashMapOf (
        "name" to nameEditText.text.toString(),
        "phoneNumber" to phoneNumberEditText.text.toString(),
        "address" to addressEditText.text.toString(),
        "postCode" to postCodeEditText.text.toString(),
        "city" to cityEditText.text.toString()
        )

        db.collection("users").document(auth.currentUser!!.uid)
            .set(userData)
            .addOnSuccessListener {
                Log.d("!!!", "success save userdata")
            }
    }

    fun displayCurrentUserData () {
        val user = auth.currentUser
        if (user != null) {
            val docRef = db.collection("users")
                .document(user.uid)

            docRef.get()
                .addOnSuccessListener { document ->
                    if(document != null) {
                        val name = document.data?.get("name")
                        val address = document.data?.get("address")
                        val phoneNumber = document.data?.get("phoneNumber")
                        val city = document.data?.get("city")
                        val postCode = document.data?.get("postCode")

                        if(name != null) {
                            nameEditText.setText(name.toString())
                        }
                        if(address != null) {
                            addressEditText.setText(address.toString())
                        }
                        if(phoneNumber != null) {
                            phoneNumberEditText.setText(phoneNumber.toString())
                        }
                        if(city != null) {
                            cityEditText.setText(city.toString())
                        }
                        if(postCode!= null) {
                            postCodeEditText.setText(postCode.toString())
                        }

                        Log.d("!!!", "namn: $name, adress: $address, mobilnr: $phoneNumber, stad: $city, postnr: $postCode")
                    } else {
                        Log.d("!!!", "document = null")
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
}