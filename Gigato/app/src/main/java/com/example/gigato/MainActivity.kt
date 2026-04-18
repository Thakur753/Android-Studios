package com.example.gigato

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // WebView ko uske ID se find karna
        val myWebView: android.webkit.WebView = findViewById(R.id.myWebView)

        // JavaScript ko enable karna zaroori hai taaki site ke animations aur cart theek se chalein
        myWebView.settings.javaScriptEnabled = true

        // Ye ensure karega ki links click karne par app ke andar hi khule, bahar Chrome browser mein nahi
        myWebView.webViewClient = android.webkit.WebViewClient()

        // Tumhari Zoho Sites wali live link yahan load hogi
        myWebView.loadUrl("https://majorptoject.zohosites.in/")
    }
}