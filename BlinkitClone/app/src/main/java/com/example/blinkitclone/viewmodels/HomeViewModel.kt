package com.example.blinkitclone.viewmodels

import androidx.lifecycle.ViewModel
import com.example.blinkitclone.models.Category
import com.example.blinkitclone.models.Order
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

    private val _cart = MutableStateFlow<List<Product>>(emptyList())
    val cart: StateFlow<List<Product>> = _cart

    private val _pastOrders = MutableStateFlow<List<Order>>(getDemoOrders())
    val pastOrders: StateFlow<List<Order>> = _pastOrders

    private val _selectedCategory = MutableStateFlow<String?>(null)
    val selectedCategory: StateFlow<String?> = _selectedCategory

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery

    init {
        fetchCategories()
        fetchProducts()
    }

    fun setSelectedCategory(categoryId: String?) {
        _selectedCategory.value = categoryId
    }

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
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

    private fun fetchProducts() {
        database.child("Products").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val list = mutableListOf<Product>()
                for (child in snapshot.children) {
                    val product = child.getValue(Product::class.java)
                    if (product != null) {
                        list.add(product)
                    }
                }
                _products.value = list
            }
            override fun onCancelled(error: DatabaseError) {}
        })
    }

    fun addToCart(product: Product) {
        val currentCart = _cart.value.toMutableList()
        val index = currentCart.indexOfFirst { it.id == product.id }
        if (index != -1) {
            val updatedProduct = currentCart[index].copy(itemCount = currentCart[index].itemCount + 1)
            currentCart[index] = updatedProduct
        } else {
            currentCart.add(product.copy(itemCount = 1))
        }
        _cart.value = currentCart
    }

    fun removeFromCart(product: Product) {
        val currentCart = _cart.value.toMutableList()
        val index = currentCart.indexOfFirst { it.id == product.id }
        if (index != -1) {
            if (currentCart[index].itemCount > 1) {
                val updatedProduct = currentCart[index].copy(itemCount = currentCart[index].itemCount - 1)
                currentCart[index] = updatedProduct
            } else {
                currentCart.removeAt(index)
            }
        }
        _cart.value = currentCart
    }

    fun placeOrder() {
        if (_cart.value.isEmpty()) return
        
        val newOrder = Order(
            orderId = "ORD${System.currentTimeMillis().toString().takeLast(6)}",
            items = _cart.value.toList(),
            totalPrice = _cart.value.sumOf { it.price!! * it.itemCount },
            status = "Placed"
        )
        
        _pastOrders.value = listOf(newOrder) + _pastOrders.value
        _cart.value = emptyList()
    }
}

fun getDemoOrders(): List<Order> {
    return listOf(
        Order("ORD12345", emptyList(), 450, "Delivered", System.currentTimeMillis() - 86400000, "Home, New Delhi"),
        Order("ORD67890", emptyList(), 120, "Placed", System.currentTimeMillis() - 3600000, "Office, Gurgaon")
    )
}
