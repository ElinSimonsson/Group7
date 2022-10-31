package com.example.group7


import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ContextThemeWrapper
import android.view.LayoutInflater

import android.widget.TextView


import androidx.appcompat.app.ActionBar

import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class MenuActivity : AppCompatActivity() {

    lateinit var auth: FirebaseAuth
    lateinit var menuTextView: TextView
    lateinit var drinkTextView : TextView
    lateinit var recyclerView: RecyclerView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)

        auth = Firebase.auth

        menuTextView = findViewById(R.id.menuTextView)
        drinkTextView = findViewById(R.id.drinkTextView)


        val restaurant = getRestaurantName()
        val actionBar: ActionBar? = supportActionBar
        actionBar?.title = restaurant
        actionBar?.setDisplayHomeAsUpEnabled(true)


        replaceWithFoodFragment()

            menuTextView.setOnClickListener {
                replaceWithFoodFragment()
            }
            drinkTextView.setOnClickListener {
                replaceWithDrinkFragment()
            }
        }

override fun onBackPressed() {
    if (DataManager.itemInCartList.isEmpty()) {
        finish()
    } else {
        warningAlertDialog()
    }
}
    fun clearListAndFinish () {
        DataManager.itemInCartList.clear()
        finish()
    }

    fun warningAlertDialog() {
        val alertDialog = android.app.AlertDialog.Builder(ContextThemeWrapper(this, R.style.AlertDialogCustom))
            .create()
        alertDialog.setMessage("Varukorgen kommer att tömmas, vill du fortsätta ändå?")
        alertDialog.setTitle("Det finns varor i din varukorg!")
        alertDialog.setCancelable(false)
        alertDialog.setButton(android.app.AlertDialog.BUTTON_POSITIVE, "Ja"
        ) { dialog, which -> clearListAndFinish() }
        alertDialog.setButton(android.app.AlertDialog.BUTTON_NEGATIVE, "Avbryt"
        ) { dialog, which -> dialog.cancel() }
        alertDialog.show()
    }

    fun customDialog () {

        val dialogBinding = layoutInflater.inflate(R.layout.custom_dialog, null)

        val dialog = Dialog(this)
        dialog.setContentView(dialogBinding)

        dialog.setCancelable(false)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.show()

        val yesButton = dialogBinding.findViewById<TextView>(R.id.yes_dialog)
        val cancelButton = dialogBinding.findViewById<TextView>(R.id.cancel_dialog)

        yesButton.setOnClickListener {
            dialog.dismiss()
            finish()
        }
        cancelButton.setOnClickListener {
            dialog.dismiss()
        }

    }


    fun replaceWithDrinkFragment () {
        val fragment = DrinkFragment()
        val bundle = Bundle()
        val restaurant = getRestaurantName()
        bundle.putString("restaurant", restaurant)
        fragment.arguments = bundle
        supportFragmentManager.beginTransaction().replace(R.id.container, fragment)
            .commit()
    }

    fun replaceWithFoodFragment() {
        val foodFragment = FoodFragment()
        val bundle = Bundle()
        val restaurant = getRestaurantName()
        bundle.putString("restaurant", restaurant)
        foodFragment.arguments = bundle
        supportFragmentManager.beginTransaction().replace(R.id.container, foodFragment)
            .commit()
    }

    fun getRestaurantName(): String {
        val restaurantName = intent.getStringExtra("restaurant").toString()

        return restaurantName
    }

     override fun onOptionsItemSelected(item: android.view.MenuItem): Boolean {
         if(DataManager.itemInCartList.isEmpty()) {
             when (item.itemId) {
                 android.R.id.home -> finish()
             }
         } else {
             when (item.itemId) {
                 android.R.id.home -> customDialog()
             }
         }
         return super.onOptionsItemSelected(item)
    }
}




