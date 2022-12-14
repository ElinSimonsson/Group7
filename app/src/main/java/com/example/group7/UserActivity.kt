package com.example.group7

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.ActionBar
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
    lateinit var userImage : ImageView
    var showOrHide = "show"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user)


        auth = Firebase.auth
        db = Firebase.firestore
        emailEditText = findViewById(R.id.emailEditText)
        passwordEditText = findViewById(R.id.passwordEditText)
        userImage = findViewById(R.id.userImage)
        val showHideBtn = findViewById<Button>(R.id.hideShowBtn)

        val actionBar: ActionBar? = supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)


        showHideBtn.setOnClickListener {

            if (showOrHide == "show") {
                passwordEditText.transformationMethod =
                    HideReturnsTransformationMethod.getInstance()
                showHideBtn.text = getString(R.string.hide_button)
                showOrHide = "hide"
            } else {
                passwordEditText.transformationMethod = PasswordTransformationMethod.getInstance()
                showHideBtn.text = getString(R.string.show_button)
                showOrHide = "show"
            }
        }

        val signInBtn = findViewById<Button>(R.id.signInBtn)
        signInBtn.setOnClickListener {

            signIn()
        }

        val signUpBtn = findViewById<Button>(R.id.signUpBtn)
        signUpBtn.setOnClickListener {

            signUp()

        }
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
                    finish()

                } else {
                    Toast.makeText(baseContext, "Signed up failed", Toast.LENGTH_LONG).show()
                    Log.d("!!!", "Fail Sign Up")
                    Log.d("!!!", "${it.exception}")
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