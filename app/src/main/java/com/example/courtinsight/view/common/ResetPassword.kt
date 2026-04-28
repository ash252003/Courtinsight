package com.example.courtinsight.view.common

import android.widget.Toast
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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Password
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.courtinsight.R
import com.example.courtinsight.viewModel.AuthViewModel

@Composable
fun ResetPassword(email: String, navController: NavController, viewModel: AuthViewModel = viewModel()) {
    var password by remember { mutableStateOf("") }
    var isPasswordVisible by remember { mutableStateOf(false) }
    var confirmPassword by remember { mutableStateOf("") }
    var isConfirmPasswordVisible by remember { mutableStateOf(false) }
    val context = LocalContext.current
    LaunchedEffect(email) {
        val msg = viewModel.toastMessage
        msg.collect {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
        }
    }
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        ElevatedCard(
            modifier = Modifier
                .width(300.dp)
                .wrapContentHeight()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Spacer(Modifier.height(16.dp))
                OutlinedTextField(
                    value = password,
                    onValueChange = {password = it},
                    label = { Text(stringResource(R.string.password)) },
                    singleLine = true,
                    isError = password.isNotEmpty() && !viewModel.isValidPassword(password),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Password
                    ),
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
                            Icon(imageVector = image, contentDescription = stringResource(
                                R.string.password_toggle
                            ),
                                tint = colorResource(R.color.bGray))
                        }
                    },
                )
                Spacer(Modifier.height(16.dp))

                OutlinedTextField(
                    value = confirmPassword,
                    onValueChange = {confirmPassword = it},
                    isError = confirmPassword.isNotEmpty() && !viewModel.isValidConfirmPassword(password, confirmPassword),
                    label = { Text(stringResource(R.string.confirm_password)) },
                    singleLine = true,
                    visualTransformation = if (isConfirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Password,
                            contentDescription = stringResource(R.string.confirm_password),
                            tint = colorResource(R.color.purple_700)
                        )
                    },
                    trailingIcon = {
                        val image = if (isConfirmPasswordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff
                        IconButton(onClick = { isConfirmPasswordVisible = !isConfirmPasswordVisible }) {
                            Icon(imageVector = image, contentDescription = stringResource(
                                R.string.password_toggle
                            ),
                                tint = colorResource(R.color.bGray))
                        }
                    },
                )
                Spacer(Modifier.height(16.dp))
                Button(
                    onClick = {
                        if (password.isNotEmpty() && confirmPassword.isNotEmpty() && viewModel.isValidPassword(password) && viewModel.isValidConfirmPassword(password, confirmPassword)){
                            viewModel.updatePassword(email, password, onSuccess = {
                                Toast.makeText(context, "Password Updated Successfully", Toast.LENGTH_SHORT).show()
                                navController.navigate("login"){
                                    popUpTo("login"){inclusive = true}
                                }
                            })
                        } else {
                            Toast.makeText(context, "Please fill all fields correctly", Toast.LENGTH_SHORT).show()
                        }
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = colorResource(R.color.app_main),
                        contentColor = colorResource(R.color.white)
                    ),
                    modifier = Modifier.fillMaxWidth().padding(8.dp),
                ) {
                    Text(stringResource(R.string.change_password))
                }
                Spacer(Modifier.height(16.dp))
            }
        }
    }
}

@Composable
@Preview(showSystemUi = true)
fun ResetPasswordPreview() {
    ResetPassword("", navController = NavController(context = LocalContext.current))
}