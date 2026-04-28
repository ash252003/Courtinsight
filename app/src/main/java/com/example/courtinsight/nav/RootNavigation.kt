package com.example.courtinsight.nav

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.courtinsight.view.common.SplashScreen

@Composable
fun RootNavigation() {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = "splash"
    ) {
        composable("auth") {
            AppNavigation(navController)
        }
        composable("user_home") {
            UserNavigation(navController)
        }
        composable("splash") {
            SplashScreen(navController)
        }
    }
}