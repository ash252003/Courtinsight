package com.example.courtinsight.view.court

import android.net.Uri
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.courtinsight.R
import com.example.courtinsight.view.common.CourtPreview
import com.example.courtinsight.viewModel.CourtViewModel

@Composable
fun ManagePost(navController: NavController) {
    val viewModel: CourtViewModel = viewModel(
        LocalContext.current as ComponentActivity
    )

    var pdfUri by remember { mutableStateOf<Uri?>(null) }
    val context = LocalContext.current
    val pdfPicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        pdfUri = uri
        if (pdfUri != null){
            viewModel.uploadPdf(context, pdfUri!!)
        }
    }
    LaunchedEffect(true) {
        viewModel.toastMessage.collect{ msg ->
            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
        }
    }
    LaunchedEffect(Unit) {
        if (viewModel.posts.isEmpty()) {
            viewModel.getAllPoster()
        }
    }
    val posters = viewModel.posts.sortedByDescending { it.date }
    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 80.dp)
        ) {
            if (posters.isNotEmpty()){
                if (viewModel.isLoading){
                    item {
                        Column(
                            modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            CircularProgressIndicator(modifier = Modifier.padding(16.dp))
                        }
                    }
                } else {
                    item {
                        if(viewModel.isUploading){
                            LinearProgressIndicator(
                                modifier = Modifier.fillMaxWidth().padding(16.dp).background(color = colorResource(R.color.bGray))
                            )
                        }
                    }
                    items(posters) { post ->
                        CourtPreview(
                            post.id, post.image, post.title, post.date, post.what_happened,
                            navController, viewModel
                        )
                    }
                }
            } else {
                item {
                    Text("No Post Available", modifier = Modifier.padding(16.dp).fillMaxWidth(), textAlign = TextAlign.Center, color = Color.DarkGray)
                }
            }
        }

        BottomAppBar(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(vertical = 0.dp, horizontal = 16.dp),
            containerColor = Color.Transparent
        ) {
            Row(
                modifier = Modifier.fillMaxWidth()
                    .background(color = Color.Transparent),
                horizontalArrangement = Arrangement.End
            ) {
                ExtendedFloatingActionButton(
                    onClick = {
                        if (!viewModel.isUploading){
                            pdfPicker.launch("application/pdf")
                        }
                    },
                    icon = {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Add Button"
                        )
                    },
                    text = { Text("Add Post") },
                    contentColor = if (viewModel.isUploading) Color.Gray else Color.White,
                    containerColor = colorResource(R.color.app_main),
                )
            }
        }
    }
}

@Composable
@Preview(showSystemUi = true)
fun ManagePostPreview() {
    val navController = NavController(LocalContext.current)
    ManagePost(navController)
}