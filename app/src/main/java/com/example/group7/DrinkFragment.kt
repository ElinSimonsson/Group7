package com.example.group7

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [MenuFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class DrinkFragment : Fragment(), DrinkRecycleAdapter.DrinkListClickListener {
    lateinit var recyclerView: RecyclerView
    lateinit var cartTextView: TextView


    var db = Firebase.firestore


    // TODO: Rename and change types of parameters
    private var param1: String? = null
    //private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
           // param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_drink, container, false)
        return view
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
            FoodFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    //putString(ARG_PARAM2, param2)
                }
            }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        cartTextView = view.findViewById(R.id.cartTextView)

        var totalItems = getTotalItems()


        cartTextView.setOnClickListener {
            for(item in DataManager.itemInCartList) {
                if (item != null) {
                    Log.d("!!!", "${item.name}")
                }
            }
            val intent = Intent(context, OrderActivity::class.java)
            startActivity(intent)
        }

        readData {
            recyclerView = view.findViewById(R.id.menuRecyclerView)
            recyclerView.layoutManager = GridLayoutManager(context, 2)
            recyclerView.adapter = DrinkRecycleAdapter(it, this)

        }

    }

    fun readData(myCallback: (MutableList<MenuItem>) -> Unit) {
        db.collection("restaurants").document(getRestaurantName()).collection("drink")
            .orderBy("name")
            .get().addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val list = mutableListOf<MenuItem>()
                    for (document in task.result) {
                        val name = document.data["name"].toString()
                        val price = document.data["price"].toString().toInt()
                        val imageURL = document.data["imageURL"].toString()
                        val menuItem = MenuItem(name, price, imageURL, 0)
                        list.add(menuItem)

                    }
                    myCallback(list)
                }
            }
    }

    fun getRestaurantName (): String {
        val data = arguments
        val restaurant = data?.get("restaurant")
        return restaurant.toString()
    }

    fun getTotalPrice () : Int {
        var totalPrice = 0
        for (item in DataManager.itemInCartList) {
            totalPrice = if (item?.totalCart!! > 1) {
                val count = item.price?.times(item.totalCart)
                totalPrice + count!!
            } else {
                totalPrice + item.price!!
            }
        }
        return totalPrice
    }
    fun getTotalItems () : Int {
        var totalItems = 0
        for (item in DataManager.itemInCartList) {
            totalItems = totalItems + item!!.totalCart
        }
        return totalItems
    }

    override fun addItemToCart(menu: MenuItem) {
        cartTextView.visibility = View.VISIBLE
        val totalItems = getTotalItems()
        val price = getTotalPrice()
        cartTextView.text = getString(R.string.cart_textview, totalItems, price)
    }

    override fun upgradeItemInCart(menu: MenuItem) {
        val totalItems = getTotalItems()
        val price = getTotalPrice()
        cartTextView.text = getString(R.string.cart_textview, totalItems, price)

    }

    override fun removeItemFromCart(menu: MenuItem) {
        val totalItems = getTotalItems()
        val price = getTotalPrice()
        if(totalItems == 0) {
            cartTextView.visibility = View.GONE
        }
        cartTextView.text = getString(R.string.cart_textview, totalItems, price)
    }
}
