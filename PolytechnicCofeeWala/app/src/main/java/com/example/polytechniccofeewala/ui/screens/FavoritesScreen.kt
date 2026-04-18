package com.example.polytechniccofeewala.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.polytechniccofeewala.model.Coffee
import com.example.polytechniccofeewala.model.coffeeList
import com.example.polytechniccofeewala.ui.theme.GreyCoffee
import com.example.polytechniccofeewala.viewmodel.CoffeeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoritesScreen(viewModel: CoffeeViewModel, onCoffeeClick: (Coffee) -> Unit, onBackClick: () -> Unit) {
    val favoriteCoffees = coffeeList.filter { viewModel.isFavorite(it.id) }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Favorites", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBackIosNew, contentDescription = null)
                    }
                }
            )
        }
    ) { padding ->
        if (favoriteCoffees.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(Icons.Default.Favorite, contentDescription = null, modifier = Modifier.size(64.dp), tint = GreyCoffee)
                    Spacer(modifier = Modifier.height(16.dp))
                    Text("No favorites yet", color = GreyCoffee, fontSize = 16.sp)
                }
            }
        } else {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier.fillMaxSize().padding(padding),
                contentPadding = PaddingValues(30.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(favoriteCoffees) { coffee ->
                    CoffeeCard(coffee = coffee, onClick = { onCoffeeClick(coffee) })
                }
            }
        }
    }
}
