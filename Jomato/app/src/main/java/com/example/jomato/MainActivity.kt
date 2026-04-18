package com.example.jomato

import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var webView: WebView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        webView = findViewById(R.id.webView)
        
        // JavaScript enable karna zaroori hai modern websites ke liye
        webView.settings.javaScriptEnabled = true
        
        // DOM Storage enable karna (agar website local storage use karti hai)
        webView.settings.domStorageEnabled = true

        // Yeh ensure karega ki links external browser mein na khule, balki app mein hi khule
        webView.webViewClient = WebViewClient()

        // Apni website ka URL load karo
        webView.loadUrl("https://jomato1.odoo.com/")

        // Back button handling logic
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (webView.canGoBack()) {
                    webView.goBack()
                } else {
                    // Agar back history nahi hai, toh app close ho jayegi
                    isEnabled = false
                    onBackPressedDispatcher.onBackPressed()
                }
            }
        })
    }
}
