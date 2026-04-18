package com.example.polytechniccofeewala

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.polytechniccofeewala.model.coffeeList
import com.example.polytechniccofeewala.ui.screens.*
import com.example.polytechniccofeewala.ui.theme.PolytechnicCofeeWalaTheme
import com.example.polytechniccofeewala.viewmodel.CoffeeViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PolytechnicCofeeWalaTheme {
                CoffeeApp()
            }
        }
    }
}

@Composable
fun CoffeeApp() {
    val navController = rememberNavController()
    val coffeeViewModel: CoffeeViewModel = viewModel()
    val context = LocalContext.current
    
    NavHost(navController = navController, startDestination = "onboarding") {
        composable("onboarding") {
            OnboardingScreen(onGetStartedClick = {
                navController.navigate("home") {
                    popUpTo("onboarding") { inclusive = true }
                }
            })
        }
        composable("home") {
            HomeScreen(
                onCoffeeClick = { coffee ->
                    navController.navigate("detail/${coffee.id}")
                },
                onCartClick = {
                    navController.navigate("cart")
                },
                onFavoriteClick = {
                    navController.navigate("favorites")
                },
                onNotificationClick = {
                    navController.navigate("notifications")
                },
                viewModel = coffeeViewModel
            )
        }
        composable("favorites") {
            FavoritesScreen(
                viewModel = coffeeViewModel,
                onCoffeeClick = { coffee ->
                    navController.navigate("detail/${coffee.id}")
                },
                onBackClick = { navController.popBackStack() }
            )
        }
        composable("notifications") {
            NotificationsScreen(onBackClick = { navController.popBackStack() })
        }
        composable("cart") {
            CartScreen(
                viewModel = coffeeViewModel,
                onBackClick = { navController.popBackStack() },
                onOrderPlaced = {
                    Toast.makeText(context, "Order Placed Successfully!", Toast.LENGTH_SHORT).show()
                    navController.navigate("home") {
                        popUpTo("home") { inclusive = true }
                    }
                }
            )
        }
        composable(
            route = "detail/{coffeeId}",
            arguments = listOf(navArgument("coffeeId") { type = NavType.IntType })
        ) { backStackEntry ->
            val coffeeId = backStackEntry.arguments?.getInt("coffeeId")
            val coffee = coffeeList.find { it.id == coffeeId }
            if (coffee != null) {
                DetailScreen(
                    coffee = coffee,
                    viewModel = coffeeViewModel,
                    onBackClick = { navController.popBackStack() },
                    onAddToCart = { selectedSize ->
                        coffeeViewModel.addToCart(coffee, selectedSize)
                        Toast.makeText(context, "Added to cart", Toast.LENGTH_SHORT).show()
                        navController.navigate("cart")
                    }
                )
            }
        }
    }
}
