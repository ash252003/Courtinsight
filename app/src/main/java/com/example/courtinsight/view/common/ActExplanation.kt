package com.example.courtinsight.view.common

import androidx.activity.ComponentActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.courtinsight.R
import com.example.courtinsight.viewModel.ActViewModel

@Composable
fun ActExplanation() {
    val viewModel: ActViewModel = viewModel(
        LocalContext.current as ComponentActivity
    )
    val result = viewModel.messages
    val focusManager = LocalFocusManager.current
    val gradientColors = listOf(colorResource(R.color.app_main), Color.Blue, Color.Cyan)
    val listState = androidx.compose.foundation.lazy.rememberLazyListState()
    LaunchedEffect(result.size) {
        if (result.isNotEmpty()) {
            listState.animateScrollToItem(0)
        }
    }
    val clipBoardManager = LocalClipboardManager.current
    Scaffold(
        bottomBar = {
            BottomAppBar(
                modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 16.dp),
                containerColor = MaterialTheme.colorScheme.surfaceContainerLow
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = viewModel.input,
                        onValueChange = { viewModel.input = it },
                        singleLine = false,
                        maxLines = 8,
                        placeholder = { Text("Type Here....") },
                        modifier = Modifier.weight(1f),
                        shape = MaterialTheme.shapes.medium
                    )
                    if (viewModel.isTyping) {
                        CircularProgressIndicator(modifier = Modifier.padding(8.dp))
                    } else {
                        IconButton(onClick = {
                            viewModel.sendMessage()
                            focusManager.clearFocus()
                        }) {
                            Icon(
                                imageVector = Icons.Default.ArrowUpward,
                                contentDescription = "Send"
                            )
                        }
                    }
                }
            }
        }
    ) { innerPadding ->
        if (result.isEmpty()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(R.drawable.logo),
                        contentDescription = "Logo",
                        modifier = Modifier
                            .width(50.dp)
                            .padding(end = 8.dp)
                    )
                    Text(
                        "CourtInsight AI", style = TextStyle(
                            brush = Brush.linearGradient(colors = gradientColors),
                            fontSize = MaterialTheme.typography.headlineMedium.fontSize
                        )
                    )
                }
                Text(
                    "Please Enter Whatever you want to know about BNS Act",
                    modifier = Modifier.padding(16.dp),
                    textAlign = TextAlign.Center
                )
            }
        } else {
            LazyColumn(
                state = listState,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                horizontalAlignment = Alignment.CenterHorizontally,
                reverseLayout = true
            ) {
                item {
                    if (viewModel.isTyping) {
                        Text(
                            "Thinking...",
                            color = Color.Gray,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 8.dp),
                            textAlign = TextAlign.Start
                        )
                    }
                }
                items(result.size) { index ->
                    val msg = result[result.size - 1 - index]
                    Column() {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = if (msg.role == "user") Arrangement.End else Arrangement.Start,
                        ) {
                            Text(
                                text = msg.text,
                                textAlign = TextAlign.Justify,
                                modifier = Modifier
                                    .width(320.dp)
                                    .padding(8.dp)
                                    .background(
                                        if (msg.role == "user") colorResource(R.color.gray_chat) else Color.White,
                                        MaterialTheme.shapes.medium
                                    )
                                    .padding(12.dp)
                            )
                        }
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                            horizontalArrangement = if (msg.role == "user") Arrangement.End else Arrangement.Start,
                        ) {
                            IconButton(
                                onClick = {
                                    clipBoardManager.setText(AnnotatedString(msg.text))
                                },
                                modifier = Modifier.size(20.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.ContentCopy,
                                    contentDescription = "Copy",
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
@Preview(showSystemUi = true)
fun ActExplanationPreview(){
    ActExplanation()
}
