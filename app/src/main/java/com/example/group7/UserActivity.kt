package com.example.group7

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.core.view.isVisible
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class UserActivity : AppCompatActivity() {

    lateinit var auth: FirebaseAuth
    lateinit var db: FirebaseFirestore
    lateinit var emailEditText: EditText
    lateinit var passwordEditText: EditText
    lateinit var adressEditText: EditText
    lateinit var saveAdressBtn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user)


        auth = Firebase.auth
        db = Firebase.firestore
        emailEditText = findViewById(R.id.emailEditText)
        passwordEditText = findViewById(R.id.passwordEditText)
        adressEditText = findViewById(R.id.adressEditText)
        saveAdressBtn = findViewById(R.id.saveAdressBtn)

        ableAdressText(false)

        ableSaveAdressBtn(false)


        val signInBtn = findViewById<Button>(R.id.signInBtn)
        signInBtn.setOnClickListener {

            signIn()
        }
        val signUpBtn = findViewById<Button>(R.id.signUpBtn)
        signUpBtn.setOnClickListener {

            signUp()

        }

        saveAdressBtn.setOnClickListener {
            saveUserData()
            finish()
        }


    }

    fun ableSaveAdressBtn(able: Boolean) {
        saveAdressBtn.isEnabled = able
        saveAdressBtn.isVisible = able
    }

    fun ableAdressText(able: Boolean) {
        adressEditText.isEnabled = able
        adressEditText.isVisible = able
    }

    private fun saveUserData() {
        val userData = UserData(adress = adressEditText.text.toString())

        val user = auth.currentUser
        if (user == null) {
            Log.d("!!!", "User not found")
            return
        }
        db.collection("users").document(user.uid)
            .collection("adress").add(userData)
        Log.d("!!!", "adress added to : ${user.email}")


    }

    private fun signIn() {
        val email = emailEditText.text.toString()
        val password = passwordEditText.text.toString()

        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    Log.d("!!!", "Signed In")
                    Toast.makeText(baseContext, "Signed in successfully", Toast.LENGTH_LONG).show()
                    finish()
                } else {
                    Toast.makeText(baseContext, "No account found", Toast.LENGTH_LONG).show()
                    Log.d("!!!", "Fail Sign in")
                    Log.d("!!!", "${it.exception}")
                }
            }
    }


    private fun signUp() {
        val email = emailEditText.text.toString()
        val password = passwordEditText.text.toString()

        if (email.isEmpty() || password.isEmpty()) {
            return
        }

        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    Log.d("!!!", "Signed Up")
                    Toast.makeText(baseContext, "Please enter home-adress below", Toast.LENGTH_LONG)
                        .show()
                    ableAdressText(true)
                    ableSaveAdressBtn(true)

                } else {
                    Toast.makeText(baseContext, "Signed up failed", Toast.LENGTH_LONG).show()
                    Log.d("!!!", "Fail Sign Up")
                    Log.d("!!!", "${it.exception}")
                }
            }

    }


}