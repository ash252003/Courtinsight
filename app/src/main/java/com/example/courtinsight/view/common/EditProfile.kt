package com.example.courtinsight.view.common

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.edit
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.courtinsight.R
import com.example.courtinsight.viewModel.AuthViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfile(emailState: String, navController: NavController, viewModel: AuthViewModel = viewModel()) {
    var name by remember(emailState) { mutableStateOf("") }
    var password by remember(emailState) { mutableStateOf("") }
    var selectedOption by remember(emailState) { mutableStateOf("Select Gender") }
    var isPasswordVisible by remember { mutableStateOf(false) }
//    var email by remember { mutableStateOf("") }
    val options = listOf("Male", "Female", "Other")
    var expanded by remember { mutableStateOf(false) }
//    var userType by remember { mutableStateOf("") }
    var img by remember { mutableStateOf(R.drawable.male) }
    LaunchedEffect(emailState) {
        viewModel.getUserDetails(emailState)
    }
    LaunchedEffect(viewModel.details) {
        viewModel.details?.let {
            name = it.name
            password = it.password
            selectedOption = it.gender
//            userType = it.user_type
//            email = it.email
            img = when (selectedOption) {
                "Male" -> R.drawable.male
                "Female" -> R.drawable.female
                else -> R.drawable.female
            }
        }
    }
    val context = LocalContext.current
    LaunchedEffect(Unit) {
        viewModel.toastMessage.collect{ msg ->
            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
        }
    }
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        if (viewModel.userLoading){
            CircularProgressIndicator(modifier = Modifier.padding(16.dp))
        } else {
            ElevatedCard(
                modifier = Modifier
                    .width(300.dp)
                    .wrapContentHeight(),
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Image(
                        painter = painterResource(img),
                        contentDescription = "Logo",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(100.dp)
                    )
                    Spacer(Modifier.height(16.dp))
                    OutlinedTextField(
                        value = name,
                        onValueChange = { name = it },
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
                            isError = name.isNotEmpty() && !viewModel.isValidGender(selectedOption),
                            singleLine = true,
                            leadingIcon = {
                                Icon(
                                    imageVector = if (selectedOption == "Select Gender") Icons.Default.PersonSearch else if (selectedOption == "Male") Icons.Default.Male else if (selectedOption == "Female") Icons.Default.Female else Icons.Default.Transgender,
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
                        value = password,
                        onValueChange = { password = it },
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
                            val image =
                                if (isPasswordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff
                            IconButton(onClick = { isPasswordVisible = !isPasswordVisible }) {
                                Icon(
                                    imageVector = image, contentDescription = stringResource(
                                        R.string.password_toggle
                                    ),
                                    tint = colorResource(R.color.bGray)
                                )
                            }
                        },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(Modifier.height(16.dp))

                    Button(
                        onClick = {
                            if(viewModel.isValidName(name) && viewModel.isValidGender(selectedOption) && viewModel.isValidPassword(password)){
                                viewModel.updateDetails(emailState, name, selectedOption, password) {
                                    Toast.makeText(context, "Profile Updated", Toast.LENGTH_SHORT)
                                        .show()
                                    navController.navigate("setting"){
                                        popUpTo("setting"){ inclusive = true }
                                    }
                                    val prefs = context.getSharedPreferences("user_session", Context.MODE_PRIVATE)
                                    prefs.edit {
                                        putString("name", name)
                                        apply()
                                    }
                                }
                            } else {
                                Toast.makeText(context, "Please fill all the fields", Toast.LENGTH_SHORT)
                                    .show()
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = Shapes().medium,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = colorResource(R.color.app_main),
                            contentColor = colorResource(R.color.white)
                        )
                    ) {
                        Text(stringResource(R.string.update_profile))
                    }
                }
            }
        }
    }
}
@Composable
@Preview(showSystemUi = true)
fun EditProfilePreview() {
    val navController = NavController(LocalContext.current)
}