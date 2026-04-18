package com.example.polytechniccofeewala.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.polytechniccofeewala.model.Coffee
import com.example.polytechniccofeewala.ui.theme.*
import com.example.polytechniccofeewala.viewmodel.CoffeeViewModel

@Composable
fun DetailScreen(
    coffee: Coffee,
    viewModel: CoffeeViewModel,
    onBackClick: () -> Unit,
    onAddToCart: (String) -> Unit
) {
    var selectedSize by remember { mutableStateOf("M") }
    val isFavorite = viewModel.isFavorite(coffee.id)

    Scaffold(
        bottomBar = {
            PriceBottomBar(price = coffee.price, onBuyClick = { onAddToCart(selectedSize) })
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 30.dp)
        ) {
            // Header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 20.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBackClick) {
                    Icon(Icons.Default.ArrowBackIosNew, contentDescription = null)
                }
                Text("Detail", fontSize = 18.sp, fontWeight = FontWeight.SemiBold)
                IconButton(onClick = { viewModel.toggleFavorite(coffee.id) }) {
                    Icon(
                        if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                        contentDescription = null,
                        tint = if (isFavorite) Color.Red else Color.Black
                    )
                }
            }

            // Image
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(226.dp)
                    .clip(RoundedCornerShape(16.dp))
            ) {
                Image(
                    painter = painterResource(id = coffee.image),
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Title and Rating
            Text(coffee.name, fontSize = 24.sp, fontWeight = FontWeight.Bold)
            Text(coffee.subtitle, color = GreyCoffee, fontSize = 12.sp)

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Star, contentDescription = null, tint = Color(0xFFFFD700))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        coffee.rating.toString(),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        " (${coffee.reviews})",
                        color = GreyCoffee,
                        fontSize = 12.sp
                    )
                }
            }

            Divider(color = LightGreyCoffee, thickness = 1.dp)

            Spacer(modifier = Modifier.height(20.dp))

            // Description
            Text("Description", fontSize = 16.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                coffee.description,
                color = GreyCoffee,
                fontSize = 14.sp,
                lineHeight = 22.sp
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Size Selection
            Text("Size", fontSize = 16.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(12.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                SizeOption("S", selectedSize == "S") { selectedSize = "S" }
                SizeOption("M", selectedSize == "M") { selectedSize = "M" }
                SizeOption("L", selectedSize == "L") { selectedSize = "L" }
            }
        }
    }
}

@Composable
fun RowScope.SizeOption(size: String, isSelected: Boolean, onClick: () -> Unit) {
    Surface(
        onClick = onClick,
        modifier = Modifier.weight(1f).height(44.dp),
        shape = RoundedCornerShape(12.dp),
        border = androidx.compose.foundation.BorderStroke(
            1.dp,
            if (isSelected) Brown else Color.LightGray
        ),
        color = if (isSelected) LightBrown.copy(alpha = 0.1f) else White
    ) {
        Box(contentAlignment = Alignment.Center) {
            Text(
                size,
                color = if (isSelected) Brown else BlackCoffee,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
            )
        }
    }
}

@Composable
fun PriceBottomBar(price: Double, onBuyClick: () -> Unit) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shadowElevation = 8.dp,
        color = White,
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(30.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text("Price", color = GreyCoffee, fontSize = 14.sp)
                Text("$ $price", color = Brown, fontSize = 18.sp, fontWeight = FontWeight.Bold)
            }
            Button(
                onClick = onBuyClick,
                modifier = Modifier
                    .width(217.dp)
                    .height(62.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Brown)
            ) {
                Text("Add to Cart", fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
            }
        }
    }
}
