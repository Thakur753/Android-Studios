package com.example.polytechniccofeewala.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.polytechniccofeewala.model.CartItem
import com.example.polytechniccofeewala.ui.theme.*
import com.example.polytechniccofeewala.viewmodel.CoffeeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartScreen(viewModel: CoffeeViewModel, onBackClick: () -> Unit, onOrderPlaced: () -> Unit) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Order", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBackIosNew, contentDescription = null)
                    }
                }
            )
        },
        bottomBar = {
            if (viewModel.cartItems.isNotEmpty()) {
                OrderBottomBar(totalPrice = viewModel.getTotalPrice(), onOrderClick = {
                    viewModel.clearCart()
                    onOrderPlaced()
                })
            }
        }
    ) { padding ->
        if (viewModel.cartItems.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Your cart is empty", color = GreyCoffee)
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(horizontal = 30.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                contentPadding = PaddingValues(vertical = 20.dp)
            ) {
                items(viewModel.cartItems) { item ->
                    CartItemRow(
                        item = item,
                        onIncrement = { viewModel.updateQuantity(item, true) },
                        onDecrement = { viewModel.updateQuantity(item, false) },
                        onRemove = { viewModel.removeFromCart(item) }
                    )
                }
            }
        }
    }
}

@Composable
fun CartItemRow(item: CartItem, onIncrement: () -> Unit, onDecrement: () -> Unit, onRemove: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(54.dp)
                .clip(RoundedCornerShape(12.dp))
        ) {
            Image(
                painter = painterResource(id = item.coffee.image),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        }
        
        Spacer(modifier = Modifier.width(12.dp))
        
        Column(modifier = Modifier.weight(1f)) {
            Text(item.coffee.name, fontWeight = FontWeight.Bold, fontSize = 16.sp)
            Text("${item.coffee.subtitle} | Size: ${item.size}", color = GreyCoffee, fontSize = 12.sp)
        }
        
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = onDecrement, modifier = Modifier.size(24.dp)) {
                Icon(Icons.Default.Remove, contentDescription = null, tint = BlackCoffee)
            }
            Text(
                item.quantity.toString(),
                modifier = Modifier.padding(horizontal = 8.dp),
                fontWeight = FontWeight.Bold
            )
            IconButton(onClick = onIncrement, modifier = Modifier.size(24.dp)) {
                Icon(Icons.Default.Add, contentDescription = null, tint = BlackCoffee)
            }
            Spacer(modifier = Modifier.width(8.dp))
            IconButton(onClick = onRemove, modifier = Modifier.size(24.dp)) {
                Icon(Icons.Default.Delete, contentDescription = null, tint = Color.Red)
            }
        }
    }
}

@Composable
fun OrderBottomBar(totalPrice: Double, onOrderClick: () -> Unit) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shadowElevation = 8.dp,
        color = White,
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
    ) {
        Column(modifier = Modifier.padding(30.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Total Payment", fontWeight = FontWeight.Bold)
                Text("$ ${String.format("%.2f", totalPrice)}", color = Brown, fontWeight = FontWeight.Bold)
            }
            Spacer(modifier = Modifier.height(20.dp))
            Button(
                onClick = onOrderClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(62.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Brown)
            ) {
                Text("Order", fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
            }
        }
    }
}
