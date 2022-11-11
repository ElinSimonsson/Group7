package com.example.group7

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [AdminDrinkFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AdminDrinkFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    lateinit var recyclerView: RecyclerView
    var db = Firebase.firestore


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
        return inflater.inflate(R.layout.fragment_admin_drink, container, false)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment AdminDrinkFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            AdminDrinkFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        readMenuData {
            recyclerView = view.findViewById(R.id.adminDrinkRV)
            recyclerView.layoutManager = GridLayoutManager(context, 2)
            val adapter = AdminDrinkAdapter(it, getResNameFragment(), DRINK)
            recyclerView.adapter = adapter

        }


    }

    fun getResNameFragment(): String {
        val data = arguments
        val restaurant = data?.get(RES_NAME_DRINK_FRAGMENT)
        return restaurant.toString()
    }

    fun readMenuData(myCallback: (MutableList<AdminMenuItem>) -> Unit) {
        Log.d("!!!", "Fun rmd DRINK")
        db.collection(RESTAURANT_STRING)
            .document(getResNameFragment())
            .collection(DRINK)
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val list = mutableListOf<AdminMenuItem>()
                    for (document in task.result) {
                        val name = document.data["name"].toString()
                        val price = document.data["price"].toString().toInt()
                        val imageURL = document.data["imageURL"].toString()
                        val documentID = document.id
                        val adminMenuItem = AdminMenuItem(documentID, name, price, imageURL)
                        list.add(adminMenuItem)
                    }
                    myCallback(list)
                }
            }
    }
}