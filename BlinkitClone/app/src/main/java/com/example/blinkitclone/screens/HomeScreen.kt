package com.example.blinkitclone.screens

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.blinkitclone.R
import com.example.blinkitclone.models.Category
import com.example.blinkitclone.models.Product
import com.example.blinkitclone.viewmodels.HomeViewModel

@Composable
fun HomeScreen(navController: NavHostController, viewModel: HomeViewModel = viewModel()) {
    val firebaseCategories by viewModel.categories.collectAsState()
    val firebaseProducts by viewModel.products.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()
    
    val categories = if (firebaseCategories.isNotEmpty()) firebaseCategories else getDemoCategories()
    val allProducts = if (firebaseProducts.isNotEmpty()) firebaseProducts else getDemoProducts()

    val filteredProducts = if (searchQuery.isEmpty()) {
        allProducts
    } else {
        allProducts.filter { it.name?.contains(searchQuery, ignoreCase = true) == true }
    }

    Scaffold(
        topBar = { TopBarSection() }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(Color(0xFFF8F8F8))
        ) {
            item {
                CustomSearchBar(searchQuery) { viewModel.setSearchQuery(it) }
                
                if (searchQuery.isEmpty()) {
                    Spacer(modifier = Modifier.height(16.dp))
                    BannerSection()
                    Spacer(modifier = Modifier.height(20.dp))
                    CategoryGridSection(categories)
                    Spacer(modifier = Modifier.height(20.dp))
                    HorizontalProductSection("Bestsellers", allProducts, viewModel)
                    Spacer(modifier = Modifier.height(20.dp))
                    HorizontalProductSection("Snacks & Munchies", allProducts.reversed(), viewModel)
                } else {
                    Spacer(modifier = Modifier.height(16.dp))
                    SearchProductGrid(filteredProducts, viewModel)
                }
                Spacer(modifier = Modifier.height(100.dp))
            }
        }
    }
}

@Composable
fun SearchProductGrid(products: List<Product>, viewModel: HomeViewModel) {
    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
        Text(text = "Search Results", fontSize = 18.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(16.dp))
        products.chunked(2).forEach { rowItems ->
            Row(modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                rowItems.forEach { product ->
                    Box(modifier = Modifier.weight(1f)) {
                        ProductItemUI(product, viewModel)
                    }
                }
                if (rowItems.size == 1) {
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
        }
    }
}

@Composable
fun CustomSearchBar(query: String, onQueryChange: (String) -> Unit) {
    OutlinedTextField(
        value = query,
        onValueChange = onQueryChange,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        placeholder = { Text(text = "Search \"chips\"", color = Color.Gray, fontSize = 14.sp) },
        leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = Color.Gray) },
        trailingIcon = { if(query.isNotEmpty()) IconButton(onClick = { onQueryChange("") }) { Icon(Icons.Default.Close, contentDescription = null) } },
        shape = RoundedCornerShape(12.dp),
        colors = OutlinedTextFieldDefaults.colors(
            unfocusedContainerColor = Color.White,
            focusedContainerColor = Color.White,
            unfocusedBorderColor = Color(0xFFEEEEEE),
            focusedBorderColor = Color(0xFF318616)
        ),
        singleLine = true
    )
}

@Composable
fun TopBarSection() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Timer, contentDescription = null, modifier = Modifier.size(20.dp), tint = Color(0xFF318616))
                Spacer(modifier = Modifier.width(4.dp))
                Text(text = "Delivery in 10 minutes", fontSize = 16.sp, fontWeight = FontWeight.ExtraBold)
            }
            Text(text = "H.No 123, New Delhi, India", fontSize = 12.sp, color = Color.Gray, maxLines = 1)
        }
        IconButton(onClick = { /* Notification */ }) {
            Icon(Icons.Default.Notifications, contentDescription = null)
        }
        Box(
            modifier = Modifier
                .size(36.dp)
                .clip(CircleShape)
                .background(Color(0xFFE8F5E9)),
            contentAlignment = Alignment.Center
        ) {
            Icon(Icons.Default.Person, contentDescription = null, modifier = Modifier.size(20.dp))
        }
    }
}

@Composable
fun BannerSection() {
    LazyRow(
        modifier = Modifier.fillMaxWidth(),
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(3) { // 3 Demo Banners
            Card(
                modifier = Modifier.width(320.dp).height(160.dp),
                shape = RoundedCornerShape(16.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.maxresdefault_12),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}

@Composable
fun CategoryGridSection(categories: List<Category>) {
    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
            Text(text = "Shop by Category", fontSize = 18.sp, fontWeight = FontWeight.Bold)
            Text(text = "See All", color = Color(0xFF318616), fontSize = 14.sp, fontWeight = FontWeight.Bold)
        }
        Spacer(modifier = Modifier.height(16.dp))
        
        // Grid
        val rows = categories.chunked(4)
        rows.forEach { rowItems ->
            Row(modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                rowItems.forEach { category ->
                    CategoryItem(category)
                }
                // Fill empty slots in the last row if needed
                repeat(4 - rowItems.size) {
                    Spacer(modifier = Modifier.width(80.dp))
                }
            }
        }
    }
}

@Composable
fun CategoryItem(category: Category) {
    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.width(80.dp)) {
        Box(
            modifier = Modifier.size(75.dp).clip(RoundedCornerShape(12.dp)).background(Color(0xFFF3F9F3)),
            contentAlignment = Alignment.Center
        ) {
            val drawableId = when(category.name?.lowercase()) {
                "vegetables" -> R.drawable.vegetable
                "dairy" -> R.drawable.dairy_breakfast
                "munchies" -> R.drawable.munchies
                "cold drinks" -> R.drawable.cold_and_juices
                "bakery" -> R.drawable.bakery_biscuits
                "cleaning" -> R.drawable.cleaning
                "baby care" -> R.drawable.baby_care
                "tea" -> R.drawable.tea_coffee
                "fruit" -> R.drawable.vegetable // Placeholder
                "meat" -> R.drawable.munchies // Placeholder
                else -> R.drawable.vegetable
            }
            Image(painter = painterResource(id = drawableId), contentDescription = null, modifier = Modifier.size(60.dp))
        }
        Text(text = category.name ?: "", fontSize = 11.sp, fontWeight = FontWeight.SemiBold, modifier = Modifier.padding(top = 4.dp), textAlign = TextAlign.Center, maxLines = 1, overflow = TextOverflow.Ellipsis)
    }
}

@Composable
fun HorizontalProductSection(title: String, products: List<Product>, viewModel: HomeViewModel) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(text = title, fontSize = 18.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp))
        LazyRow(contentPadding = PaddingValues(horizontal = 16.dp), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            items(products) { product ->
                ProductItemUI(product, viewModel)
            }
        }
    }
}

@Composable
fun ProductItemUI(product: Product, viewModel: HomeViewModel) {
    Card(
        modifier = Modifier.width(150.dp).border(0.5.dp, Color(0xFFEEEEEE), RoundedCornerShape(12.dp)),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(modifier = Modifier.padding(8.dp)) {
            Box(modifier = Modifier.fillMaxWidth().height(100.dp), contentAlignment = Alignment.TopEnd) {
                if (!product.image.isNullOrEmpty()) {
                    AsyncImage(
                        model = product.image,
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Fit,
                        placeholder = painterResource(id = R.drawable.image_prod)
                    )
                } else {
                    Image(painter = painterResource(id = R.drawable.image_prod), contentDescription = null, modifier = Modifier.fillMaxSize())
                }
                Surface(color = Color(0xFF318616), shape = RoundedCornerShape(4.dp), modifier = Modifier.padding(4.dp)) {
                    Text(text = "10% OFF", color = Color.White, fontSize = 8.sp, modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp))
                }
            }
            Text(text = product.name ?: "", fontSize = 13.sp, fontWeight = FontWeight.Bold, maxLines = 2, modifier = Modifier.height(36.dp), overflow = TextOverflow.Ellipsis)
            Text(text = product.unit ?: "", fontSize = 11.sp, color = Color.Gray)
            Spacer(modifier = Modifier.height(8.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Column {
                    Text(text = "₹${product.price}", fontSize = 14.sp, fontWeight = FontWeight.ExtraBold)
                    Text(text = "₹${(product.price ?: 0) + 10}", fontSize = 10.sp, color = Color.Gray, style = androidx.compose.ui.text.TextStyle(textDecoration = androidx.compose.ui.text.style.TextDecoration.LineThrough))
                }
                
                val cartItems by viewModel.cart.collectAsState()
                val cartItem = cartItems.find { it.id == product.id }
                
                if (cartItem == null || cartItem.itemCount == 0) {
                    Button(
                        onClick = { viewModel.addToCart(product) },
                        modifier = Modifier.height(30.dp).width(60.dp),
                        contentPadding = PaddingValues(0.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color.White, contentColor = Color(0xFF318616)),
                        shape = RoundedCornerShape(6.dp),
                        border = BorderStroke(1.dp, Color(0xFF318616))
                    ) {
                        Text("ADD", fontSize = 12.sp, fontWeight = FontWeight.ExtraBold)
                    }
                } else {
                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = Color(0xFF318616),
                        modifier = Modifier.height(30.dp).width(70.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceEvenly) {
                            Text("-", color = Color.White, modifier = Modifier.clickable { viewModel.removeFromCart(product) }, fontWeight = FontWeight.Bold)
                            Text(text = cartItem.itemCount.toString(), color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            Text("+", color = Color.White, modifier = Modifier.clickable { viewModel.addToCart(product) }, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}

fun getDemoCategories(): List<Category> {
    return listOf(
        Category("1", "Vegetables", ""), Category("2", "Dairy", ""),
        Category("3", "Munchies", ""), Category("4", "Cold Drinks", ""),
        Category("5", "Bakery", ""), Category("6", "Cleaning", ""),
        Category("7", "Baby Care", ""), Category("8", "Tea", ""),
        Category("9", "Fruit", ""), Category("10", "Meat", ""),
        Category("11", "Pet Care", ""), Category("12", "Frozen Food", "")
    )
}

fun getDemoProducts(): List<Product> {
    return listOf(
        Product("1", "Banana (Kela)", 35, "1 kg (6-8 pcs)", "https://cdn.shopify.com/s/files/1/0451/1101/7626/products/Banana_1_1024x1024.jpg?v=1602146443"),
        Product("2", "Amul Butter", 56, "100 g", ""),
        Product("3", "Brown Bread", 45, "400 g", ""),
        Product("4", "Lays Chips", 20, "50 g", ""),
        Product("5", "Coca Cola", 40, "750 ml", ""),
        Product("6", "Onion (Pyaz)", 40, "1 kg", ""),
        Product("7", "Tomato (Tamatar)", 30, "500 g", ""),
        Product("8", "Apple (Seb)", 120, "1 kg", "")
    )
}
