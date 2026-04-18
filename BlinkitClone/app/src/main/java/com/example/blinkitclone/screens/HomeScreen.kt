package com.example.blinkitclone.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.blinkitclone.models.Category

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.blinkitclone.viewmodels.HomeViewModel

@Composable
fun HomeScreen(navController: NavHostController, viewModel: HomeViewModel = viewModel()) {
    val categories by viewModel.categories.collectAsState()

    Scaffold(
        topBar = { HomeTopBar() }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(Color(0xFFF1F1F1))
        ) {
            SearchBar()
            Spacer(modifier = Modifier.height(10.dp))
            CategorySection(categories)
        }
    }
}

@Composable
fun HomeTopBar() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(16.dp)
    ) {
        Text(
            text = "Delivery in 10 minutes",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )
        Text(
            text = "H.No 123, New Delhi, India",
            fontSize = 14.sp,
            color = Color.Gray
        )
    }
}

@Composable
fun SearchBar() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(Color.White)
            .padding(horizontal = 12.dp, vertical = 8.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                painter = painterResource(id = com.example.blinkitclone.R.drawable.baseline_search_24),
                contentDescription = null,
                tint = Color.Gray,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = "Search \"milk\"", color = Color.Gray, fontSize = 14.sp)
        }
    }
}

@Composable
fun CategorySection(categories: List<Category>) {
    Column(modifier = Modifier.padding(16.dp)) {
        Text(
            text = "Shop by Category",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 12.dp)
        )
        LazyVerticalGrid(
            columns = GridCells.Fixed(4),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.height(260.dp)
        ) {
            items(categories) { category ->
                CategoryItem(category)
            }
        }
    }
}

@Composable
fun CategoryItem(category: Category) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.width(80.dp)
    ) {
        Box(
            modifier = Modifier
                .size(75.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(Color(0xFFF5F5F5)),
            contentAlignment = Alignment.Center
        ) {
            val imageSource = category.image
            if (imageSource != null && imageSource.startsWith("http")) {
                AsyncImage(
                    model = imageSource,
                    contentDescription = category.name,
                    modifier = Modifier.size(60.dp),
                    contentScale = ContentScale.Fit
                )
            } else {
                Image(
                    painter = painterResource(id = com.example.blinkitclone.R.drawable.vegetable),
                    contentDescription = category.name,
                    modifier = Modifier.size(60.dp),
                    contentScale = ContentScale.Fit
                )
            }
        }
        Text(
            text = category.name ?: "",
            fontSize = 10.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(top = 6.dp),
            maxLines = 2,
            lineHeight = 12.sp,
            textAlign = androidx.compose.ui.text.style.TextAlign.Center
        )
    }
}
