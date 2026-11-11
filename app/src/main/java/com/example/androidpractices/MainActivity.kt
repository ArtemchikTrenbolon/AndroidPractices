package com.example.androidpractices

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.androidpractices.ui.MainScreen
import com.example.androidpractices.ui.theme.AndroidPracticesTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AndroidPracticesTheme {
                MainScreen()
            }
        }
    }
}
