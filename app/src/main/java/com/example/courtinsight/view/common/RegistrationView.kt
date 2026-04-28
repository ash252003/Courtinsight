package com.example.courtinsight.view.common

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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Female
import androidx.compose.material.icons.filled.Male
import androidx.compose.material.icons.filled.Password
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PersonSearch
import androidx.compose.material.icons.filled.Transgender
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.courtinsight.R
import com.example.courtinsight.viewModel.AuthViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegistrationView(navController: NavController, viewModel: AuthViewModel = viewModel()){
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isPasswordVisible by remember { mutableStateOf(false) }
    var confirmPassword by remember { mutableStateOf("") }
    var isConfirmPasswordVisible by remember { mutableStateOf(false) }
    val options = listOf("Male", "Female", "Other")
    var expanded by remember { mutableStateOf(false) }
    var selectedOption by remember { mutableStateOf("Select Gender") }
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
                    .width(320.dp)
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
                            .height(160.dp)
                    )
                    OutlinedTextField(
                        value = name,
                        onValueChange = {name = it},
                        label = { Text(stringResource(R.string.name)) },
                        singleLine = true,
                        isError = name.isNotEmpty() && !viewModel.isValidName(name),
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Person,
                                contentDescription = stringResource(R.string.name),
                                tint = colorResource(R.color.purple_700)
                            )
                        },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(Modifier.height(16.dp))

                    ExposedDropdownMenuBox(
                        expanded = expanded,
                        onExpandedChange = { expanded = !expanded }
                    ) {
                        OutlinedTextField(
                            value = selectedOption,
                            onValueChange = {},
                            readOnly = true,
                            isError = email.isNotEmpty() && !viewModel.isValidGender(selectedOption),
                            singleLine = true,
                            leadingIcon = {
                                Icon(
                                    imageVector = if(selectedOption == "Select Gender") Icons.Default.PersonSearch else if (selectedOption == "Male") Icons.Default.Male else if (selectedOption == "Female") Icons.Default.Female else Icons.Default.Transgender,
                                    contentDescription = stringResource(R.string.name),
                                    tint = colorResource(R.color.purple_700)
                                )
                            },
                            trailingIcon = {
                                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .menuAnchor(
                                    type = MenuAnchorType.PrimaryEditable,
                                    enabled = true
                                )
                        )

                        ExposedDropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false }
                        ) {
                            options.forEach { item ->
                                DropdownMenuItem(
                                    text = { Text(item) },
                                    onClick = {
                                        selectedOption = item
                                        expanded = false
                                    }
                                )
                            }
                        }
                    }
                    Spacer(Modifier.height(16.dp))
                    OutlinedTextField(
                        value = email,
                        onValueChange = {email = it},
                        label = { Text(stringResource(R.string.email)) },
                        singleLine = true,
                        isError = email.isNotEmpty() && !viewModel.isValidEmail(email),
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Email,
                                contentDescription = stringResource(R.string.email),
                                tint = colorResource(R.color.purple_700)
                            )
                        },
                        modifier = Modifier.fillMaxWidth()
                    )
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
                        modifier = Modifier.fillMaxWidth()
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
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(Modifier.height(16.dp))
                    if(viewModel.isRegistrationLoading){
                        CircularProgressIndicator(modifier = Modifier.padding(16.dp))
                    } else {
                        Button(
                            onClick = {
                                if(viewModel.isValidName(name) && viewModel.isValidEmail(email) && viewModel.isValidPassword(password)
                                    && viewModel.isValidConfirmPassword(password, confirmPassword) && viewModel.isValidGender(selectedOption)){
                                    viewModel.checkEmail(email, onResult = { exists ->
                                        if(exists){
                                            Toast.makeText(context, "Email Already Exists", Toast.LENGTH_SHORT).show()
                                        } else{
                                            viewModel.addUser(name, email, password, selectedOption){
                                                Toast.makeText(context, "Registration Successful", Toast.LENGTH_SHORT).show()
                                                navController.popBackStack()
                                            }
                                        }
                                    })
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
                            Text(stringResource(R.string.register))
                        }
                    }
                    Spacer(Modifier.height(16.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) { Text(text = "Already Have An Account?",
                        color = colorResource(R.color.black),
                        modifier = Modifier.padding(end = 4.dp),
                        fontWeight = FontWeight.Bold
                    )
                        Text(text = stringResource(R.string.login),
                            color = colorResource(R.color.purple_700),
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.clickable{navController.navigate("login")}
                        )
                    }
                }
            }
        }
    }
}

@Composable
@Preview(showSystemUi = true)
fun RegistrationPreview(){
    RegistrationView(navController = NavController(context = LocalContext.current))
}