package com.example.blinkitclone.models

data class Product(
    val id: String? = null,
    val name: String? = null,
    val price: Int? = null,
    val unit: String? = null,
    val image: String? = null,
    val categoryId: String? = null,
    val itemCount: Int = 0,
    val rating: Float = 4.5f,
    val deliveryTime: String = "10 mins",
    val stockCount: Int = 10 // New field for real-time stock
)
