package com.example.polytechniccofeewala.model

data class CartItem(
    val coffee: Coffee,
    var quantity: Int = 1,
    val size: String = "M"
)
