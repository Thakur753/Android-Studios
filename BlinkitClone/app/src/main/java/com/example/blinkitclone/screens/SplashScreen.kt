package com.example.blinkitclone.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.blinkitclone.R
import com.example.blinkitclone.navigation.Routes
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(navController: NavHostController) {
    // Navigate to Home after 2 seconds (or Auth if not logged in)
    LaunchedEffect(Unit) {
        delay(2000)
        navController.navigate(Routes.Auth.route) {
            popUpTo(Routes.Splash.route) { inclusive = true }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8CB46)), // Blinkit yellow
        contentAlignment = Alignment.Center
    ) {
        // You would typically have a logo here. 
        // For now, I'll use a placeholder or just leave it as a yellow screen if logo is missing.
        // Image(painter = painterResource(id = R.drawable.blinkit_logo), contentDescription = null, modifier = Modifier.size(200.dp))
    }
}
