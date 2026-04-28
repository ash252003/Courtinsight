@file:Suppress("DEPRECATION")

package com.example.courtinsight.view.common

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.SentimentDissatisfied
import androidx.compose.material.icons.outlined.SentimentNeutral
import androidx.compose.material.icons.outlined.SentimentSatisfied
import androidx.compose.material.icons.outlined.SentimentVeryDissatisfied
import androidx.compose.material.icons.outlined.SentimentVerySatisfied
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.courtinsight.R
import com.example.courtinsight.viewModel.UserViewModel

@Composable
fun Feedback(navController: NavController, viewModel: UserViewModel = viewModel()) {
    var selectedEmoji by remember { mutableStateOf(-1) }
    var feedbackText by remember { mutableStateOf("") }
    val context = LocalContext.current
    val icons = listOf(
        Icons.Outlined.SentimentVeryDissatisfied,
        Icons.Outlined.SentimentDissatisfied,
        Icons.Outlined.SentimentNeutral,
        Icons.Outlined.SentimentSatisfied,
        Icons.Outlined.SentimentVerySatisfied
    )
    LaunchedEffect(Unit) {
        viewModel.toastMessage.collect { message ->
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        }
    }
    val sharedPreferences =
        context.getSharedPreferences("user_session", Context.MODE_PRIVATE)
    val email = sharedPreferences.getString("email", "") ?: ""
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        item {
            Column(modifier = Modifier.height(500.dp).width(300.dp),
                verticalArrangement = Arrangement.SpaceEvenly) {
                Column {
                    Text("Give Feedback", fontWeight = FontWeight.Bold,
                        fontSize = MaterialTheme.typography.headlineSmall.fontSize, modifier = Modifier.fillMaxWidth().padding(bottom = 10.dp))
                    Text("What do you think about CourtInsight?", fontSize = MaterialTheme.typography.titleMedium.fontSize,
                        modifier = Modifier.fillMaxWidth().padding(bottom = 25.dp))
                }
                Row(modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically) {
                    icons.forEachIndexed { index, icon ->
                        Icon(
                            imageVector = icons[index],
                            contentDescription = "Emoji",
                            modifier = Modifier.size(50.dp).clickable{selectedEmoji = index},
                            tint = if (selectedEmoji == index) colorResource(R.color.emoji) else Color.Gray
                        )
                    }
                }
                Column {
                    Text("Do you have any thoughts you'd like to share?", fontSize = MaterialTheme.typography.labelLarge.fontSize, fontWeight = FontWeight.Bold,
                        modifier = Modifier.fillMaxWidth().padding(top = 25.dp))
                    OutlinedTextField(
                        value = feedbackText,
                        onValueChange = { feedbackText = it },
                        placeholder = { Text("Start typing here...") },
                        modifier = Modifier.padding(top = 10.dp).height(150.dp).fillMaxWidth(),
                        maxLines = 4,
                        singleLine = false,
                        isError = feedbackText.isNotEmpty() && !viewModel.validateFeedback(feedbackText),
                        shape = MaterialTheme.shapes.medium
                    )
                }
                Row(modifier = Modifier.fillMaxWidth().padding(top = 20.dp),
                    horizontalArrangement = Arrangement.SpaceBetween) {
                    if (viewModel.feedBackLoading) {
                        CircularProgressIndicator(modifier = Modifier.padding(16.dp))
                    } else {
                        Button(
                            onClick = {
                                if(viewModel.validateFeedback(feedbackText) && selectedEmoji != -1){
                                    viewModel.addFeedBack(email, feedbackText, selectedEmoji + 1)
                                    navController.navigate("setting"){
                                        popUpTo("setting"){inclusive = true}
                                    }
                                } else {
                                    Toast.makeText(context, "Please fill all fields correctly", Toast.LENGTH_SHORT).show()
                                }
                            },
                            modifier = Modifier.height(50.dp).width(130.dp),
                            shape = MaterialTheme.shapes.medium,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = colorResource(R.color.app_main),
                                contentColor = colorResource(R.color.white)
                            )
                        ) {
                            Text("Send")
                        }

                        Button(
                            onClick = {
                                navController.navigate("setting"){
                                    popUpTo("setting"){inclusive = true}
                                }
                            },
                            modifier = Modifier.height(50.dp).width(130.dp),
                            shape = MaterialTheme.shapes.medium,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = colorResource(R.color.white),
                                contentColor = Color.DarkGray
                            ),
                            border = ButtonDefaults.outlinedButtonBorder
                        ) {
                            Text("Cancel")
                        }
                    }
                }
            }
        }
    }
}

@Composable
@Preview(showSystemUi = true)
fun FeedbackPreview() {
    val navController = rememberNavController()
    Feedback(navController)
}