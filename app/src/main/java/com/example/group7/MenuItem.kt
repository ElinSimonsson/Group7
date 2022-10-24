package com.example.group7

data class MenuItem(
    var name: String? = null,
    var price: Int? = 0,
    var imageURL: String? = null,
    var totalCart: Int
) {
}

data class Restaurants(var name: String?) {

}
