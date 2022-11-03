package com.example.group7

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [OrderFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class OrderFragment : Fragment() {
    lateinit var db : FirebaseFirestore
    lateinit var recyclerView: RecyclerView

    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

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
        return inflater.inflate(R.layout.fragment_order, container, false)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment OrderFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            OrderFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        db = Firebase.firestore
        val restaurantName = getRestaurantName()

        fetchDocumentIdData {
            recyclerView = view.findViewById(R.id.orderRecyclerView)
            val linearLayoutManager: LinearLayoutManager = LinearLayoutManager(context)
            linearLayoutManager.orientation = LinearLayoutManager.VERTICAL
            recyclerView.layoutManager = linearLayoutManager
            val adapter = OrderRecycleAdapter(it, restaurantName)
            recyclerView.adapter = adapter
        }
    }

    fun fetchDocumentIdData (myCallback : (MutableList<DocumentId>)-> Unit) {

        val docRef = db.collection("Order").document(getRestaurantName())
            .collection("userOrders")

        docRef.addSnapshotListener { snapshot, e ->
            if (snapshot != null) {
                val listOfDocumentId = mutableListOf<DocumentId>()
                for(document in snapshot.documents) {
                    val documentId = document.id
                    Log.d("!!!", "documentid: $documentId")

                    val id = DocumentId(documentId)
                    listOfDocumentId.add(id)
                }
                myCallback(listOfDocumentId)
            }
        }
    }

    fun getRestaurantName() : String {
        val data = arguments
        val restaurant = data?.get("restaurant")
        return restaurant.toString()
    }
}