package com.example.blinkitclone.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.blinkitclone.models.Category
import com.example.blinkitclone.models.ChatMessage
import com.example.blinkitclone.models.Order
import com.example.blinkitclone.models.Product
import com.google.firebase.database.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {
    private val database = FirebaseDatabase.getInstance().reference

    private val _categories = MutableStateFlow<List<Category>>(emptyList())
    val categories: StateFlow<List<Category>> = _categories

    private val _products = MutableStateFlow<List<Product>>(emptyList())
    val products: StateFlow<List<Product>> = _products

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _cart = MutableStateFlow<List<Product>>(emptyList())
    val cart: StateFlow<List<Product>> = _cart

    private val _pastOrders = MutableStateFlow<List<Order>>(getDemoOrders())
    val pastOrders: StateFlow<List<Order>> = _pastOrders

    private val _selectedCategory = MutableStateFlow<String?>(null)
    val selectedCategory: StateFlow<String?> = _selectedCategory

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery

    // AI Chatbot States
    private val _chatMessages = MutableStateFlow<List<ChatMessage>>(listOf(ChatMessage("Hi! I am your Blinkit Assistant. How can I help you today?", false)))
    val chatMessages: StateFlow<List<ChatMessage>> = _chatMessages

    private val _isBotTyping = MutableStateFlow(false)
    val isBotTyping: StateFlow<Boolean> = _isBotTyping

    // Countdown Timer State (10 minutes in seconds)
    private val _remainingTime = MutableStateFlow(600)
    val remainingTime: StateFlow<Int> = _remainingTime

    private val _isDemoMode = MutableStateFlow(false)
    val isDemoMode: StateFlow<Boolean> = _isDemoMode

    private var categoriesLoaded = false
    private var productsLoaded = false

    init {
        fetchCategories()
        fetchProducts()
        startCountdownTimer()
        
        // Timeout for loading shimmer (max 5 seconds)
        viewModelScope.launch {
            delay(5000)
            if (_isLoading.value) {
                _isLoading.value = false
            }
        }
    }

    fun toggleDemoMode() {
        _isDemoMode.value = !_isDemoMode.value
        _isLoading.value = false
    }

    private fun startCountdownTimer() {
        viewModelScope.launch {
            while (_remainingTime.value > 0) {
                delay(1000)
                _remainingTime.value -= 1
            }
        }
    }

    fun sendMessage(text: String) {
        if (text.isBlank()) return
        
        val userMessage = ChatMessage(text, true)
        _chatMessages.value = _chatMessages.value + userMessage
        
        viewModelScope.launch {
            _isBotTyping.value = true
            delay(1500) // Simulation delay
            val response = getBotResponse(text.lowercase())
            _chatMessages.value = _chatMessages.value + ChatMessage(response, false)
            _isBotTyping.value = false
        }
    }

    private fun getBotResponse(input: String): String {
        return when {
            input.contains("order") || input.contains("delivery") || input.contains("late") -> 
                "Don't worry! Your order is being packed and will reach you within 10 minutes."
            input.contains("coupon") || input.contains("offer") || input.contains("discount") -> 
                "Use coupon code 'BLINKNEW' to get flat 20% off on your first three orders!"
            input.contains("refund") || input.contains("cancel") -> 
                "If you want to cancel, please go to the Order History section. Refunds are processed within 5 minutes to your original payment mode."
            else -> "I am your Blinkit Assistant. You can ask me about your Order Status, Latest Offers, or Refund policies!"
        }
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
                categoriesLoaded = true
                checkLoadingStatus()
            }
            override fun onCancelled(error: DatabaseError) {
                categoriesLoaded = true
                checkLoadingStatus()
            }
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
                productsLoaded = true
                checkLoadingStatus()
            }
            override fun onCancelled(error: DatabaseError) {
                productsLoaded = true
                checkLoadingStatus()
            }
        })
    }

    private fun checkLoadingStatus() {
        if (categoriesLoaded && productsLoaded) {
            _isLoading.value = false
        }
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
