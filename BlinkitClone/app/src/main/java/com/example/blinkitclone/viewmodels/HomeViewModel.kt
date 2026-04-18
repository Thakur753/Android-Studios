package com.example.blinkitclone.viewmodels

import androidx.lifecycle.ViewModel
import com.example.blinkitclone.models.Category
import com.example.blinkitclone.models.Product
import com.google.firebase.database.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class HomeViewModel : ViewModel() {
    private val database = FirebaseDatabase.getInstance("https://blinkit-clone-d586f-default-rtdb.firebaseio.com/").reference

    private val _categories = MutableStateFlow<List<Category>>(emptyList())
    val categories: StateFlow<List<Category>> = _categories

    private val _products = MutableStateFlow<List<Product>>(emptyList())
    val products: StateFlow<List<Product>> = _products

    init {
        fetchCategories()
    }

    private fun fetchCategories() {
        database.child("Categories").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val list = mutableListOf<Category>()
                for (child in snapshot.children) {
                    val category = child.getValue(Category::class.java)
                    if (category != null) {
                        list.add(category)
                    }
                }
                _categories.value = list
            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }
}
