package com.example.courtinsight.view.common

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
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
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Shapes
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import androidx.core.content.edit
import com.example.courtinsight.viewModel.ActViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserType(navController: NavController, viewModel: AuthViewModel = viewModel(), actViewModel: ActViewModel = viewModel()) {
    val options = listOf("user", "court")
    val context = LocalContext.current
    val sharedPreferences = context.getSharedPreferences("user_session", Context.MODE_PRIVATE)
    val currentUserType = sharedPreferences.getString("user_type", "")
    val email = sharedPreferences.getString("email", "")
    var expanded by remember { mutableStateOf(false) }
    var selectedOption by remember { mutableStateOf("Select User Type") }
    var password by remember { mutableStateOf("") }
    var isPasswordVisible by remember { mutableStateOf(false) }
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ElevatedCard(
            modifier = Modifier.width(350.dp).padding(top = 20.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Spacer(Modifier.padding(10.dp))
                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = { expanded = !expanded }
                ) {
                    OutlinedTextField(
                        value = selectedOption,
                        onValueChange = {},
                        readOnly = true,
                        singleLine = true,
                        leadingIcon = {
                            Icon(
                                imageVector = if (selectedOption == "user") Icons.Default.Person else if (selectedOption == "court") Icons.Default.AccountBalance else Icons.Default.PersonSearch,
                                contentDescription = stringResource(R.string.name),
                                tint = colorResource(R.color.purple_700)
                            )
                        },
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                        },
                        modifier = Modifier.width(280.dp)
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
                Spacer(Modifier.padding(10.dp))
                if (selectedOption == "court"){
                    OutlinedTextField(
                        value = password,
                        onValueChange = {password = it},
                        label = { Text("Admin PIN") },
                        singleLine = true,
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
                        modifier = Modifier.width(280.dp).padding(bottom = 10.dp)
                    )
                }
                Button(
                    onClick = {
                        if(selectedOption != "Select User Type" && selectedOption != currentUserType){
                            if (selectedOption == "court" && password.isNotEmpty()){
                                viewModel.getAdminKey(onSuccess = { pin ->
                                    if (pin == password){
                                        viewModel.updateUserType(email.toString(), selectedOption, onSuccess = {
                                            Toast.makeText(context, "User Type Updated", Toast.LENGTH_SHORT).show()
                                            sharedPreferences.edit { clear() }
                                            actViewModel.clearChat()
                                            navController.navigate("auth"){
                                                popUpTo("auth"){ inclusive = true }
                                            }
                                        })
                                    } else {
                                        Toast.makeText(context, "Invalid PIN", Toast.LENGTH_SHORT).show()
                                    }
                                })
                            } else if(selectedOption == "user"){
                                viewModel.updateUserType(email.toString(), selectedOption, onSuccess = {
                                    Toast.makeText(context, "User Type Updated", Toast.LENGTH_SHORT).show()
                                    sharedPreferences.edit {
                                        clear()
                                        apply()
                                    }
                                    navController.navigate("auth"){
                                        popUpTo("user_home"){ inclusive = true }
                                    }
                                })
                            }
                        } else {
                            Toast.makeText(context, "Please select different user type", Toast.LENGTH_SHORT).show()
                        }
                    },
                    modifier = Modifier.width(280.dp),
                    shape = Shapes().medium,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = colorResource(R.color.app_main),
                        contentColor = colorResource(R.color.white)
                    )
                ) {
                    Text("Update User Type")
                }
                Spacer(Modifier.padding(10.dp))
            }
        }
    }
}

@Composable
@Preview(showSystemUi = true)
fun UserTypePreview() {
    UserType(
        navController = NavController(LocalContext.current)
    )
}