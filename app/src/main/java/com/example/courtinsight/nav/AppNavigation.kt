package com.example.courtinsight.nav

import android.content.Context
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.courtinsight.R
import com.example.courtinsight.view.common.ForgotPassword
import com.example.courtinsight.view.common.LoginView
import com.example.courtinsight.view.common.OtpVerify
import com.example.courtinsight.view.common.RegistrationView
import com.example.courtinsight.view.common.ResetPassword
import com.example.courtinsight.view.common.SplashScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppNavigation(rootNavController: NavHostController){
    val navController = rememberNavController()
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("CourtInsight")},
                colors = topAppBarColors(
                    containerColor = colorResource(R.color.app_main),
                    titleContentColor = colorResource(R.color.white)
                )
            )
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = "login",
            modifier = Modifier.padding(innerPadding)
        ){
            composable("login") {
                LoginView(navController, rootNavController)
            }
            composable("register") {
                RegistrationView(navController)
            }
            composable("forgot_password") {
                ForgotPassword(navController)
            }
            composable("otp_verify/{email}/{otp}") {
                val otp = it.arguments?.getString("otp")
                val email = it.arguments?.getString("email")
                if (otp != null && email != null) {
                    OtpVerify(email, otp, navController)
                }
            }
            composable("reset_password/{email}") {
                val email = it.arguments?.getString("email")
                if (email != null){
                    ResetPassword(email, navController)
                }
            }
        }
    }
}