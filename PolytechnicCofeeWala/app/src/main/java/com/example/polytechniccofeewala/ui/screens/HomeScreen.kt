package com.example.polytechniccofeewala.ui.screens

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.polytechniccofeewala.model.Coffee
import com.example.polytechniccofeewala.model.coffeeList
import com.example.polytechniccofeewala.ui.theme.*
import com.example.polytechniccofeewala.viewmodel.CoffeeViewModel

@Composable
fun HomeScreen(
    onCoffeeClick: (Coffee) -> Unit,
    onCartClick: () -> Unit,
    onFavoriteClick: () -> Unit,
    onNotificationClick: () -> Unit,
    viewModel: CoffeeViewModel
) {
    var selectedCategory by remember { mutableStateOf("Cappuccino") }
    val categories = listOf("Cappuccino", "Macheato", "Latte", "Americano")
    val context = LocalContext.current

    // Filter coffee list based on search and category
    val filteredList = coffeeList.filter { 
        it.name.contains(viewModel.searchQuery, ignoreCase = true) &&
        (selectedCategory == "All" || it.name.contains(selectedCategory, ignoreCase = true) || it.subtitle.contains(selectedCategory, ignoreCase = true))
    }

    Scaffold(
        bottomBar = { 
            BottomNavigationBar(
                onHomeClick = { /* Already here */ },
                onFavoriteClick = onFavoriteClick,
                onCartClick = onCartClick,
                onNotificationClick = onNotificationClick
            ) 
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(LightGreyCoffee)
        ) {
            // Header Section
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(BlackCoffee)
                    .padding(horizontal = 30.dp, vertical = 40.dp)
            ) {
                Column {
                    Text("Location", color = Color.Gray, fontSize = 12.sp)
                    Row(
                        modifier = Modifier.clickable { 
                            Toast.makeText(context, "Changing location...", Toast.LENGTH_SHORT).show()
                        },
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            "Bilaspur, Chhattisgarh",
                            color = White,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                        Icon(
                            Icons.Default.KeyboardArrowDown,
                            contentDescription = null,
                            tint = White
                        )
                    }

                    Spacer(modifier = Modifier.height(25.dp))

                    // Search Bar
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        TextField(
                            value = viewModel.searchQuery,
                            onValueChange = viewModel::updateSearchQuery,
                            modifier = Modifier
                                .weight(1f)
                                .height(52.dp)
                                .clip(RoundedCornerShape(16.dp)),
                            placeholder = { Text("Search coffee", color = GreyCoffee) },
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Default.Search,
                                    contentDescription = null,
                                    tint = White
                                )
                            },
                            colors = TextFieldDefaults.colors(
                                focusedContainerColor = Color(0xFF313131),
                                unfocusedContainerColor = Color(0xFF313131),
                                focusedIndicatorColor = Color.Transparent,
                                unfocusedIndicatorColor = Color.Transparent,
                                focusedTextColor = White
                            )
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Box(
                            modifier = Modifier
                                .size(52.dp)
                                .background(Brown, RoundedCornerShape(12.dp))
                                .clickable { 
                                    Toast.makeText(context, "Filters coming soon!", Toast.LENGTH_SHORT).show()
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.Tune, contentDescription = null, tint = White)
                        }
                    }
                }
            }

            // Categories
            Spacer(modifier = Modifier.height(24.dp))
            LazyRow(
                contentPadding = PaddingValues(horizontal = 30.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(categories) { category ->
                    CategoryItem(
                        name = category,
                        isSelected = selectedCategory == category,
                        onClick = { selectedCategory = category }
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Coffee Grid
            if (filteredList.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("No coffee found", color = GreyCoffee)
                }
            } else {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    contentPadding = PaddingValues(horizontal = 30.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(filteredList) { coffee ->
                        CoffeeCard(coffee = coffee, onClick = { onCoffeeClick(coffee) })
                    }
                }
            }
        }
    }
}

@Composable
fun CategoryItem(name: String, isSelected: Boolean, onClick: () -> Unit) {
    Surface(
        modifier = Modifier.clickable { onClick() },
        color = if (isSelected) Brown else White,
        shape = RoundedCornerShape(12.dp)
    ) {
        Text(
            text = name,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            color = if (isSelected) White else BlackCoffee,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
        )
    }
}

@Composable
fun CoffeeCard(coffee: Coffee, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = White)
    ) {
        Column(modifier = Modifier.padding(8.dp)) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(132.dp)
                    .clip(RoundedCornerShape(12.dp))
            ) {
                Image(
                    painter = painterResource(id = coffee.image),
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
                Row(
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.Star, contentDescription = null, tint = Color(0xFFFFD700), modifier = Modifier.size(14.dp))
                    Text(coffee.rating.toString(), color = White, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                }
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            Text(coffee.name, fontWeight = FontWeight.Bold, fontSize = 16.sp)
            Text(coffee.subtitle, color = GreyCoffee, fontSize = 12.sp)
            
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("$ ${coffee.price}", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .background(Brown, RoundedCornerShape(8.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.Add, contentDescription = null, tint = White)
                }
            }
        }
    }
}

@Composable
fun BottomNavigationBar(
    onHomeClick: () -> Unit,
    onFavoriteClick: () -> Unit,
    onCartClick: () -> Unit,
    onNotificationClick: () -> Unit
) {
    NavigationBar(
        containerColor = White,
        tonalElevation = 8.dp
    ) {
        NavigationBarItem(
            selected = true,
            onClick = onHomeClick,
            icon = { Icon(Icons.Default.Home, contentDescription = null) },
            colors = NavigationBarItemDefaults.colors(selectedIconColor = Brown, unselectedIconColor = GreyCoffee)
        )
        NavigationBarItem(
            selected = false,
            onClick = onFavoriteClick,
            icon = { Icon(Icons.Default.Favorite, contentDescription = null) },
            colors = NavigationBarItemDefaults.colors(unselectedIconColor = GreyCoffee)
        )
        NavigationBarItem(
            selected = false,
            onClick = onCartClick,
            icon = { Icon(Icons.Default.ShoppingBag, contentDescription = null) },
            colors = NavigationBarItemDefaults.colors(unselectedIconColor = GreyCoffee)
        )
        NavigationBarItem(
            selected = false,
            onClick = onNotificationClick,
            icon = { Icon(Icons.Default.Notifications, contentDescription = null) },
            colors = NavigationBarItemDefaults.colors(unselectedIconColor = GreyCoffee)
        )
    }
}
