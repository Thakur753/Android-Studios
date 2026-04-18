package com.example.blinkitclone.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavHostController
import com.example.blinkitclone.navigation.Routes

@Composable
fun MainScreen(parentNavController: NavHostController) {
    var selectedItem by remember { mutableIntStateOf(0) }
    
    val items = listOf(
        Triple("Home", Routes.Home.route, Icons.Default.Home),
        Triple("Categories", "categories", Icons.AutoMirrored.Filled.List),
        Triple("Cart", "cart", Icons.Default.ShoppingCart),
        Triple("Profile", "profile", Icons.Default.Person)
    )

    Scaffold(
        bottomBar = {
            NavigationBar(containerColor = Color.White) {
                items.forEachIndexed { index, item ->
                    NavigationBarItem(
                        icon = { Icon(item.third, contentDescription = item.first) },
                        label = { Text(item.first) },
                        selected = selectedItem == index,
                        onClick = { selectedItem = index },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = Color(0xFF318616),
                            selectedTextColor = Color(0xFF318616),
                            indicatorColor = Color(0xFFE8F5E9)
                        )
                    )
                }
            }
        }
    ) { padding ->
        Box(modifier = Modifier.padding(padding)) {
            when (selectedItem) {
                0 -> HomeScreen(parentNavController)
                1 -> CategoriesScreen()
                2 -> CartScreen()
                3 -> ProfileScreen()
            }
        }
    }
}

@Composable
fun CategoriesScreen() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text("Categories Screen")
    }
}

@Composable
fun CartScreen() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text("Cart Screen")
    }
}

@Composable
fun ProfileScreen() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text("Profile Screen")
    }
}
