package com.example.group7

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class UserActivity : AppCompatActivity() {

    lateinit var auth: FirebaseAuth
    lateinit var emailEditText: EditText
    lateinit var passwordEditText: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user)


        auth = Firebase.auth
        emailEditText = findViewById(R.id.emailEditText)
        passwordEditText = findViewById(R.id.passwordEditText)


        val signInBtn = findViewById<Button>(R.id.signInBtn)
        signInBtn.setOnClickListener{

            signIn()
        }
        val signUpBtn = findViewById<Button>(R.id.signUpBtn)
        signUpBtn.setOnClickListener{

            signUp()
        }



    }
    private fun signIn() {
        val email = emailEditText.text.toString()
        val password = passwordEditText.text.toString()

        auth.signInWithEmailAndPassword(email,password)
            .addOnCompleteListener {
                if(it.isSuccessful){
                    Log.d("!!!","Signed In")
                }else {
                    Log.d("!!!","Fail Sign in")
                    Log.d("!!!","${it.exception}")
                }
            }
    }


    private fun signUp() {
        val email = emailEditText.text.toString()
        val password = passwordEditText.text.toString()

        if(email.isEmpty() || password.isEmpty()){
            return
        }

        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if(it.isSuccessful){
                    Log.d("!!!","Signed Up")
                }else{
                    Log.d("!!!","Fail Sign Up")
                    Log.d("!!!","${it.exception}")
                }
            }

    }


}