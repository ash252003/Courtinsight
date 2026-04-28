package com.example.courtinsight

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.view.WindowCompat
import com.example.courtinsight.nav.RootNavigation
import com.example.courtinsight.ui.theme.CourtInsightTheme
import com.google.firebase.FirebaseApp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        try {
            FirebaseApp.initializeApp(this)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        enableEdgeToEdge()
        setContent {
            CourtInsightTheme {
                RootNavigation()
            }
        }
    }
}