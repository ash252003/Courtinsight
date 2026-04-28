package com.example.courtinsight.view.common

import android.content.Context
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.courtinsight.R
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(navController: NavController) {
    val context = LocalContext.current
    val sharedPreferences = context.getSharedPreferences("user_session", Context.MODE_PRIVATE)
    val isLoggedIn = sharedPreferences.getBoolean("isLoggedIn", false)
    val name = sharedPreferences.getString("name", null)
    LaunchedEffect(Unit) {
        delay(3000)
        val start = if (isLoggedIn && name != null) {
            "user_home"
        } else {
            "auth"
        }
        navController.navigate(start) {
            popUpTo("splash") { inclusive = true }
        }
    }
    Box(modifier = Modifier.fillMaxSize()
        .paint(
            painterResource(R.drawable.intro_bg),
            contentScale = androidx.compose.ui.layout.ContentScale.FillBounds
        ),
    ){
        Column(modifier = Modifier.fillMaxWidth().height(300.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center) {
            Row(modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically) {
                Image(
                    painter = painterResource(R.drawable.logo),
                    contentDescription = "Logo",
                    modifier = Modifier
                        .height(45.dp)
                        .padding(end = 10.dp)
                )
                Text(stringResource(R.string.app_name), fontWeight = FontWeight.Bold,
                    fontSize = MaterialTheme.typography.headlineMedium.fontSize, color = colorResource(R.color.app_main)
                )
            }
            Row(modifier = Modifier.fillMaxWidth().padding(top = 5.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically) {
                Text("Where law meets insight.", fontSize = MaterialTheme.typography.titleMedium.fontSize)
            }
        }
    }
}

@Composable
@Preview(showSystemUi = true)
fun SplashScreenPreview() {
    val navController = rememberNavController()
    SplashScreen(navController)
}