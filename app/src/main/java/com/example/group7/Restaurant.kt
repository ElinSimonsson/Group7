package com.example.group7

data class Restaurant(var name : String? = null, var address: String? = null, var image: String? = null, var menu: List<Menu?>?) {


}

data class Menu(val name: String? = null, var price: Int, val imageURL: String? = null, var totalInCart: Int)