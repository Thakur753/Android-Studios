package com.example.blinkitclone.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.blinkitclone.screens.AuthScreen
import com.example.blinkitclone.screens.MainScreen
import com.example.blinkitclone.screens.OTPScreen
import com.example.blinkitclone.screens.SplashScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Routes.Splash.route) {
        composable(Routes.Splash.route) {
            SplashScreen(navController)
        }
        composable(Routes.Auth.route) {
            AuthScreen(navController)
        }
        composable("otp/{verificationId}") { backStackEntry ->
            val verificationId = backStackEntry.arguments?.getString("verificationId") ?: ""
            OTPScreen(navController, verificationId)
        }
        composable(Routes.Main.route) {
            MainScreen(navController)
        }
        composable("support_chat") {
            val viewModel: com.example.blinkitclone.viewmodels.HomeViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
            com.example.blinkitclone.screens.SupportChatScreen(navController, viewModel)
        }
    }
}
