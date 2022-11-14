package com.example.group7

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class AdminMenuFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    lateinit var db: FirebaseFirestore
    private var count = 0
    private var menuFragmentIsRunning = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_admin_menu, container, false)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment MenuFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            AdminMenuFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val foodButton = view.findViewById<TextView>(R.id.adminMatTextView)
        val drinkButton = view.findViewById<TextView>(R.id.adminDryckTextView)
        db = Firebase.firestore
        startFoodFragment()
        listenerUpdateOrder()

        foodButton.setOnClickListener {
            startFoodFragment()
        }

        drinkButton.setOnClickListener {
            startDrinkFragment()
        }


        val restaurantName = getRestaurantName()

        val fab = view.findViewById<FloatingActionButton>(R.id.floatingActionButton)
        fab.setOnClickListener {
            val intentFab = Intent(context, AdminDisplayItem_Activity::class.java)
            intentFab.putExtra("newUser", 1)
            intentFab.putExtra("restaurantNameFAB", restaurantName)
            startActivity(intentFab)
        }
    }

    override fun onPause() {
        super.onPause()
        Log.d("!!!", "onpause menuFragment!")
        menuFragmentIsRunning = false
    }

    override fun onResume() {
        super.onResume()
        menuFragmentIsRunning = true
    }

    fun listenerUpdateOrder() {
        val docRef = db.collection("Order").document(getRestaurantName())
            .collection("userOrders")

        docRef.addSnapshotListener { snapshot, e ->
            if (snapshot != null) {
                if (count == 0) {
                    count += 1
                } else {
                    if (menuFragmentIsRunning) {
                        showNewOrderCustomDialog()
                    }
                }
            }
        }
    }

    private fun showNewOrderCustomDialog() {
        val dialogBinding = layoutInflater.inflate(R.layout.new_order_custom_dialog, null)
        val dialog = context?.let { Dialog(it) }
        dialog?.setContentView(dialogBinding)
        dialog?.setCancelable(true)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog?.show()

        val goToOrderFragmentButton = dialogBinding.findViewById<TextView>(R.id.goToOrderFragmentTV)

        goToOrderFragmentButton.setOnClickListener {
            dialog?.dismiss()
            replaceWithOrderFragment()
        }
    }

    private fun replaceWithOrderFragment() {
        val orderFragment = OrderFragment()
        val bundle = Bundle()
        bundle.putString("restaurant", getRestaurantName())
        orderFragment.arguments = bundle
        val fragmentManager = parentFragmentManager
        val transaction = fragmentManager.beginTransaction()
        transaction.replace(R.id.adminContainer, orderFragment)
        transaction.commit()
    }

    private fun startFoodFragment() {
        val menuFragment = AdminFoodFragment()
        val bundle = Bundle()
        bundle.putString(
            RES_NAME_MENU_FRAGMENT,
            getRestaurantName()
        ) // Att göra : skapa intent för att hämta restaurangens namn
        menuFragment.arguments = bundle
        val fragmentManager = parentFragmentManager
        val transaction = fragmentManager.beginTransaction()
        transaction.replace(R.id.menuContainer, menuFragment)
        transaction.commit()
    }

    private fun startDrinkFragment() {
        val adminFragment = AdminDrinkFragment()
        val bundle = Bundle()
        bundle.putString(RES_NAME_DRINK_FRAGMENT, getRestaurantName())
        adminFragment.arguments = bundle
        val fragmentManager = parentFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.menuContainer, adminFragment)
        fragmentTransaction.commit()
    }

    fun getRestaurantName(): String {
        val data = arguments
        val restaurant = data?.get("restaurant")
        Log.d("!!!", "mottagen restaurang namn: $restaurant")
        return restaurant.toString()
    }
}









