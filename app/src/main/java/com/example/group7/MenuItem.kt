package com.example.group7

import com.google.firebase.firestore.DocumentId

data class MenuItem(
    var name: String? = null,
    var price: Int? = 0,
    var imageURL: String? = null,
    var totalCart: Int
) {
}

data class Restaurants(var name: String?, var menu: MutableList<MenuItem?>?)

data class Order (var name: String? = null, var amount: Int? = null, var price: Int = 0)

data class DocumentId(@DocumentId var documentId: String? = null)
