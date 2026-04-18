package com.example.polytechniccofeewala.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.polytechniccofeewala.model.CartItem
import com.example.polytechniccofeewala.model.Coffee

class CoffeeViewModel : ViewModel() {
    private val _cartItems = mutableStateListOf<CartItem>()
    val cartItems: List<CartItem> get() = _cartItems

    private val _favorites = mutableStateListOf<Int>() // List of Coffee IDs
    val favorites: List<Int> get() = _favorites

    var searchQuery by mutableStateOf("")
        private set

    fun updateSearchQuery(query: String) {
        searchQuery = query
    }

    fun toggleFavorite(coffeeId: Int) {
        if (_favorites.contains(coffeeId)) {
            _favorites.remove(coffeeId)
        } else {
            _favorites.add(coffeeId)
        }
    }

    fun isFavorite(coffeeId: Int): Boolean {
        return _favorites.contains(coffeeId)
    }

    fun addToCart(coffee: Coffee, size: String) {
        val existingItem = _cartItems.find { it.coffee.id == coffee.id && it.size == size }
        if (existingItem != null) {
            existingItem.quantity++
        } else {
            _cartItems.add(CartItem(coffee, 1, size))
        }
    }

    fun removeFromCart(cartItem: CartItem) {
        _cartItems.remove(cartItem)
    }

    fun updateQuantity(cartItem: CartItem, increment: Boolean) {
        val index = _cartItems.indexOf(cartItem)
        if (index != -1) {
            if (increment) {
                _cartItems[index] = _cartItems[index].copy(quantity = _cartItems[index].quantity + 1)
            } else if (_cartItems[index].quantity > 1) {
                _cartItems[index] = _cartItems[index].copy(quantity = _cartItems[index].quantity - 1)
            }
        }
    }

    fun clearCart() {
        _cartItems.clear()
    }

    fun getTotalPrice(): Double {
        return _cartItems.sumOf { it.coffee.price * it.quantity }
    }
}
