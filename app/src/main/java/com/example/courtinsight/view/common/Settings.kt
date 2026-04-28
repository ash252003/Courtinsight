package com.example.courtinsight.view.common

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowRight
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Feedback
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.edit
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.courtinsight.R
import com.example.courtinsight.model.SettingItem
import com.example.courtinsight.viewModel.ActViewModel
import com.example.courtinsight.viewModel.AuthViewModel

@Composable
fun Settings(navController: NavController, rootNavController: NavController, viewModel: AuthViewModel = viewModel(), actViewModel: ActViewModel = viewModel()) {
    val context = LocalContext.current
    val sharedPreferences = context.getSharedPreferences("user_session", Context.MODE_PRIVATE)
    val name = sharedPreferences.getString("name", null)
    val email = sharedPreferences.getString("email", null)
    val items = listOf(
        SettingItem(1, "Change User Type", Icons.Default.Person, "User Type", colorResource(R.color.purple_700), "user_type"),
        SettingItem(2, "Feedback", Icons.Default.Feedback, "Feedback", colorResource(R.color.purple_700), "feedback"),
        SettingItem(3, "About", Icons.Default.Description, "About", colorResource(R.color.purple_700), "about"),
        SettingItem(4, "Logout", Icons.AutoMirrored.Filled.Logout, "Logout", Color.Red, "login"),
    )
    var img by remember { mutableStateOf(R.drawable.male) }
    LaunchedEffect(email) {
        viewModel.getGender(email ?: "", onResult = {
            img = if (it == "Male") R.drawable.male else R.drawable.female
        })
    }
    Column(
        modifier = Modifier.fillMaxSize(),
    ) {
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            item {
                Row(modifier = Modifier.fillMaxWidth().height(150.dp).padding(top = 16.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically) {
                    Image(
                        painter = painterResource(img),
                        contentDescription = "Profile"
                    )
                }
                Column(
                    modifier = Modifier.fillMaxWidth().wrapContentHeight().padding(top = 8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(name.toString(), fontSize = MaterialTheme.typography.titleMedium.fontSize, fontWeight = FontWeight.Bold)
                    Text(email.toString(), fontSize = MaterialTheme.typography.titleSmall.fontSize)
                    Spacer(modifier = Modifier.padding(8.dp))
                    Button(
                        onClick = {
                            navController.navigate("edit_profile/$email")
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = colorResource(R.color.app_main),
                            contentColor = colorResource(R.color.white)
                        )
                    ) {
                        Text("Edit Profile")
                    }
                }
                Column(
                    modifier = Modifier.fillMaxWidth().wrapContentHeight(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    ElevatedCard(
                        elevation = CardDefaults.cardElevation(6.dp),
                        modifier = Modifier.width(330.dp).wrapContentHeight().padding(top = 16.dp),
                    ) {
                        Column(
                            modifier = Modifier.fillMaxWidth().wrapContentHeight(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            items.forEach { item ->
                                Row(
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.fillMaxWidth().height(55.dp).padding(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 8.dp).clickable{
                                        if(item.route == "login"){
                                            sharedPreferences.edit {
                                                clear()
                                                apply()
                                            }
                                            actViewModel.clearChat()

                                            rootNavController.navigate("auth"){
                                                popUpTo(0){ inclusive = true }
                                            }
                                        } else if(item.route == "feedback"){
                                            navController.navigate("feedback");
                                        } else if(item.route == "about"){
                                            navController.navigate("about")
                                        } else if (item.route == "user_type"){
                                            navController.navigate("user_type")
                                        }
                                    }
                                ) {
                                    Row {
                                        Icon(
                                            imageVector = item.imageResId,
                                            contentDescription = item.contentDescription,
                                            tint = item.color
                                        )
                                        Text(item.title, fontSize = MaterialTheme.typography.titleMedium.fontSize,
                                            modifier = Modifier.padding(start = 10.dp), color = item.color)
                                    }
                                    Icon(imageVector = Icons.AutoMirrored.Filled.ArrowRight,
                                        contentDescription = "Arrow",
                                        tint = item.color
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}


@SuppressLint("ViewModelConstructorInComposable")
@Composable
@Preview(showSystemUi = true)
fun SettingsPreview(){
    val navController = rememberNavController()
    val viewModel = AuthViewModel()
    Settings(navController, navController, viewModel)
}