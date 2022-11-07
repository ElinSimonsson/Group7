package com.example.group7

import com.google.firebase.firestore.DocumentId

class UserData(@DocumentId var documentId : String? = null,
                var adress : String? = null,
                var order : List<MenuItem>? = null){
}