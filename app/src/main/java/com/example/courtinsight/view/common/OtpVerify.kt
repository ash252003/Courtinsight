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
import androidx.compose.material.icons.filled.Security
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.navigation.NavController
import com.example.courtinsight.R

@Composable
fun OtpVerify(email: String, otp: String, navController: NavController) {
    var enteredOtp by remember { mutableStateOf("") }
    val context = LocalContext.current
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
                    value = enteredOtp,
                    onValueChange = {enteredOtp = it},
                    label = { Text(stringResource(R.string.enter_otp)) },
                    singleLine = true,
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Security,
                            contentDescription = stringResource(R.string.email),
                            tint = colorResource(R.color.purple_700)
                        )
                    }
                )
                Spacer(Modifier.height(16.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(end = 16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.End
                ) {
                    Button(
                        onClick = {
                            if (enteredOtp.trim().isNotEmpty() && enteredOtp.equals(otp)){
                                Toast.makeText(context, "OTP Verified Successfully", Toast.LENGTH_SHORT).show()
                                navController.navigate("reset_password/$email"){
                                    popUpTo("reset_password"){inclusive = true}
                                }
                            } else {
                                Toast.makeText(context, "Please enter a valid OTP", Toast.LENGTH_SHORT).show()
                            }
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = colorResource(R.color.app_main),
                            contentColor = colorResource(R.color.white)
                        )
                    ) {
                        Text(stringResource(R.string.verify))
                    }
                }
                Spacer(Modifier.height(16.dp))
            }
        }
    }
}

@Composable
@Preview(showSystemUi = true)
fun OtpVerifyPreview() {
    val navController = NavController(context = LocalContext.current)
    OtpVerify("", "", navController)
}