package com.example.blinkitclone.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.blinkitclone.navigation.Routes

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.FirebaseException
import android.app.Activity
import androidx.compose.ui.platform.LocalContext
import java.util.concurrent.TimeUnit

@Composable
fun AuthScreen(navController: NavHostController) {
    var phoneNumber by remember { mutableStateOf("") }
    val context = LocalContext.current
    val auth = FirebaseAuth.getInstance()

    // Firebase Callback for OTP
    val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        override fun onVerificationCompleted(credential: com.google.firebase.auth.PhoneAuthCredential) {
            auth.signInWithCredential(credential).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    navController.navigate(Routes.Main.route) { 
                        popUpTo(Routes.Auth.route) { inclusive = true } 
                    }
                }
            }
        }

        override fun onVerificationFailed(e: FirebaseException) {
            // Handle error
        }

        override fun onCodeSent(verificationId: String, token: PhoneAuthProvider.ForceResendingToken) {
            navController.navigate("otp/$verificationId")
        }
    }

    Scaffold { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color.White),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = com.example.blinkitclone.R.drawable.maxresdefault_12),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "India's last minute app",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Log in or sign up",
                fontSize = 14.sp,
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(24.dp))

            OutlinedTextField(
                value = phoneNumber,
                onValueChange = { input -> if (input.length <= 10) phoneNumber = input },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
                placeholder = { Text("Enter phone number") },
                leadingIcon = { 
                    Row(
                        modifier = Modifier.padding(start = 12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("+91 ", fontWeight = FontWeight.Bold, color = Color.Black)
                    }
                },
                shape = RoundedCornerShape(12.dp),
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFF318616),
                    unfocusedBorderColor = Color.LightGray
                )
            )

            Spacer(modifier = Modifier.height(16.dp))
            
            Button(
                onClick = { 
                    if (phoneNumber.length == 10) {
                        val options = PhoneAuthOptions.newBuilder(auth)
                            .setPhoneNumber("+91$phoneNumber")
                            .setTimeout(60L, TimeUnit.SECONDS)
                            .setActivity(context as Activity)
                            .setCallbacks(callbacks)
                            .build()
                        PhoneAuthProvider.verifyPhoneNumber(options)
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF318616)),
                shape = RoundedCornerShape(12.dp),
                enabled = phoneNumber.length == 10
            ) {
                Text(text = "Continue", fontSize = 16.sp, color = Color.White)
            }
        }
    }
}
