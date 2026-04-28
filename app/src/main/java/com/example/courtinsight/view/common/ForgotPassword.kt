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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.courtinsight.R
import com.example.courtinsight.viewModel.AuthViewModel

@Composable
fun ForgotPassword(navController: NavController, viewModel: AuthViewModel = viewModel()) {
    var email by remember { mutableStateOf("") }
    val context = LocalContext.current
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        ElevatedCard(
            modifier = Modifier.width(300.dp).wrapContentHeight()
        ) {
            Column(
                modifier = Modifier.fillMaxWidth().wrapContentHeight(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Spacer(Modifier.height(16.dp))
                OutlinedTextField(
                    value = email,
                    onValueChange = {email = it},
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
                Row(
                    modifier = Modifier.fillMaxWidth().padding(end = 16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.End
                ) {
                    if(viewModel.isOtpLoading){
                        CircularProgressIndicator(modifier = Modifier.padding(16.dp))
                    } else {
                        Button(
                            onClick = {
                                if(viewModel.isValidEmail(email)){
                                    viewModel.checkEmail(email, onResult = { exists ->
                                        if (exists){
                                            viewModel.sendEmailScope(email, onResult = { otp ->
                                                if(otp != null){
                                                    Toast.makeText(context, "OTP Sent Successfully", Toast.LENGTH_SHORT).show()
                                                    navController.navigate("otp_verify/$email/$otp"){
                                                        popUpTo("otp_verify"){inclusive = true}
                                                    }
                                                }
                                            })
                                        } else {
                                            Toast.makeText(context, "Email Does Not Exists", Toast.LENGTH_SHORT).show()
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
                            modifier = Modifier.width(150.dp).align(Alignment.CenterVertically)
                        ) {
                            Text("Send OTP")
                        }
                    }
                }
                Spacer(Modifier.height(16.dp))
            }
        }
    }
}

@Composable
@Preview(showSystemUi = true)
fun ForgotPasswordPreview(){
    val navController = NavController(context = LocalContext.current)
    ForgotPassword(navController)
}
