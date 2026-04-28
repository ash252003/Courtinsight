package com.example.courtinsight.view.common

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Password
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Shapes
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.edit
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.courtinsight.R
import com.example.courtinsight.viewModel.AuthViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginView(navController: NavController, rootNavController: NavController, viewModel: AuthViewModel = viewModel()){
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isPasswordVisible by remember { mutableStateOf(false) }
    val context = LocalContext.current
    LaunchedEffect(Unit) {
        viewModel.toastMessage.collect{ msg ->
            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
        }
    }
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .imePadding()
            .padding(horizontal = 16.dp, vertical = 20.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            ElevatedCard(elevation = CardDefaults.cardElevation(6.dp),
                modifier = Modifier
                    .width(300.dp)
                    .wrapContentHeight(),
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Image(
                        painter = painterResource(R.drawable.ci_logo),
                        contentDescription = "Logo",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                    )

                    OutlinedTextField(
                        value = email,
                        onValueChange = {email = it},
                        isError = email.isNotEmpty() && !viewModel.isValidEmail(email),
                        label = { Text(stringResource(R.string.email)) },
                        singleLine = true,
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Email,
                                contentDescription = stringResource(R.string.email),
                                tint = colorResource(R.color.purple_700)
                            )
                        }
                    )
                    Spacer(Modifier.height(16.dp))
                    OutlinedTextField(
                        value = password,
                        onValueChange = {password = it},
                        label = { Text(stringResource(R.string.password)) },
                        singleLine = true,
                        visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Password,
                                contentDescription = stringResource(R.string.password),
                                tint = colorResource(R.color.purple_700)
                            )
                        },
                        trailingIcon = {
                            val image = if (isPasswordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff
                            IconButton(onClick = { isPasswordVisible = !isPasswordVisible }) {
                                Icon(imageVector = image, contentDescription = stringResource(R.string.password_toggle),
                                    tint = colorResource(R.color.bGray))
                            }
                        }
                    )
                    Spacer(Modifier.height(16.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.End
                    ) { Text(text = stringResource(R.string.forgot_password),
                        color = colorResource(R.color.purple_700),
                        modifier = Modifier.padding(end = 8.dp).clickable{
                            navController.navigate("forgot_password")
                        },
                        fontWeight = FontWeight.Bold
                    )
                    }
                    Spacer(Modifier.height(16.dp))
                    if(viewModel.isLoginLoading){
                        CircularProgressIndicator(modifier = Modifier.padding(16.dp))
                    } else {
                        Button(
                            onClick = {
                                if(viewModel.isValidEmail(email) && viewModel.isValidPassword(password)){
                                    viewModel.checkLogin(email, password){ userType ->
                                        viewModel.getName(email, onResult = { name ->
                                            saveLogin(context, userType.toString(), name.toString(), email)
                                            rootNavController.navigate("user_home"){
                                                popUpTo("auth"){inclusive = true}
                                            }
                                        })
                                    }
                                } else {
                                    Toast.makeText(context, "Please fill all fields correctly", Toast.LENGTH_SHORT).show()
                                }
                            },
                            modifier = Modifier.fillMaxWidth(),
                            shape = Shapes().medium,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = colorResource(R.color.app_main),
                                contentColor = colorResource(R.color.white)
                            )
                        ) {
                            Text("Login")
                        }
                    }
                    Spacer(Modifier.height(16.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) { Text(text = stringResource(R.string.new_user),
                        color = colorResource(R.color.black),
                        modifier = Modifier.padding(end = 4.dp),
                        fontWeight = FontWeight.Bold
                    )
                        Text(text = stringResource(R.string.register),
                            color = colorResource(R.color.purple_700),
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.clickable{navController.navigate("register")}
                        )
                    }
                }
            }
        }
    }
}

fun saveLogin(context: Context, userType: String, name: String, email: String){
    val sharedPreferences = context.getSharedPreferences("user_session", Context.MODE_PRIVATE)
    sharedPreferences.edit {
        putBoolean("isLoggedIn", true)
        putString("user_type", userType)
        putString("name", name)
        putString("email", email)
        apply()
    }
}

@Composable
@Preview(showSystemUi = true)
fun LoginPreview(){
    val navController = rememberNavController()
    LoginView(navController, navController)
}