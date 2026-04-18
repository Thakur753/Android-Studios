package com.example.blinkitclone.navigation

sealed class Routes(val route: String) {
    object Splash : Routes("splash")
    object Auth : Routes("auth")
    object OTP : Routes("otp/{verificationId}") {
        fun createRoute(verificationId: String) = "otp/$verificationId"
    }
    object Main : Routes("main")
    object Home : Routes("home")
    object Category : Routes("category/{categoryName}") {
        fun createRoute(categoryName: String) = "category/$categoryName"
    }
}
