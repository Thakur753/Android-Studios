package com.example.blinkitclone.models

data class Order(
    val orderId: String? = null,
    val items: List<Product> = emptyList(),
    val totalPrice: Int = 0,
    val status: String = "Received", // Received, Packed, Out for Delivery, Delivered
    val timestamp: Long = System.currentTimeMillis(),
    val address: String = "H.No 123, New Delhi, India"
)
