package com.example.group7

import com.google.firebase.firestore.DocumentId

class AdminMenuItem(@DocumentId var documentId: String? = null,
                    var name : String? = null,
                    var price : Int? = 0,
                    var imageURL: String? = null) {

}