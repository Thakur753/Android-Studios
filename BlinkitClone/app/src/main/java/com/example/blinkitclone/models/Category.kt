package com.example.blinkitclone.models

data class Category(
    val id: String? = null,
    val name: String? = null,
    val image: String? = null // Database usually stores image URLs or Int IDs
)
