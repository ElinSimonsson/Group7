package com.example.group7

import com.google.firebase.firestore.DocumentId

class AdminMenuItem(@DocumentId var documentId: String? = null,
                    var name : String? = null,
                    var price : Int? = 0,
                    var imageURL: String? = "https://firebasestorage.googleapis.com/v0/b/group7-acaa7.appspot.com/o/No_image_available.png?alt=media&token=9f69eae8-7c9c-4897-86f2-91a86d5b945d"
) {

}