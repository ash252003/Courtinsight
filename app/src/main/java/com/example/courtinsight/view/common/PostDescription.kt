package com.example.courtinsight.view.common

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.courtinsight.R
import com.example.courtinsight.viewModel.CourtViewModel

@Composable
fun PostDescription(id: String, viewModel: CourtViewModel = viewModel()) {
    LaunchedEffect(id) {
        viewModel.getPostById(id)
    }
    val post = viewModel.selectedPost
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            if(viewModel.isLoading){
                CircularProgressIndicator(modifier = Modifier.padding(16.dp))
            } else {
                Column {
                    ElevatedCard(
                        modifier = Modifier.fillMaxWidth().wrapContentHeight().padding(16.dp, 8.dp)
                    ) {
                        AsyncImage(
                            model = post?.image,
                            contentDescription = "Post",
                            modifier = Modifier.fillMaxWidth().height(150.dp),
                            contentScale = ContentScale.Crop
                        )
                    }
                    Text("${post?.date}", modifier = Modifier.padding(16.dp, 2.dp)
                        .fillMaxWidth(), textAlign = TextAlign.End, color = colorResource(R.color.app_main), fontWeight = FontWeight.Bold
                    )
                    ElevatedCard(modifier = Modifier.fillMaxWidth().wrapContentHeight().padding(16.dp, 8.dp)) {
                        Text(post?.title ?: "", modifier = Modifier.padding(16.dp, 8.dp).fillMaxWidth().wrapContentHeight(),
                            fontWeight = FontWeight.Bold, fontSize = MaterialTheme.typography.titleMedium.fontSize)
                    }
                    Elevation("What Happened", post?.what_happened)
                    Elevation("Case Details", post?.court_said)
                    Elevation("Court Decisions", post?.court_update)
                    Text("Note: The content is derived from public court records, is intended for legal awareness and transparency purposes only, and does not constitute legal advice",
                        modifier = Modifier.padding(8.dp, 4.dp).fillMaxWidth().wrapContentHeight(),
                        textAlign = TextAlign.Center, color = Color.DarkGray, fontSize = MaterialTheme.typography.labelMedium.fontSize)
                }
            }
        }
    }
}

@Composable
fun Elevation(title: String, content: String?){
    val clipBoardManager = LocalClipboardManager.current
    ElevatedCard(
        modifier = Modifier.fillMaxWidth().wrapContentHeight().padding(16.dp, 8.dp)
    ) {
        Text("${title}:", modifier = Modifier.padding(16.dp, 8.dp).fillMaxWidth().wrapContentHeight(),
            fontWeight = FontWeight.Bold, fontSize = MaterialTheme.typography.titleMedium.fontSize)
        Text(content?: "",
            modifier = Modifier.padding(16.dp, 8.dp).fillMaxWidth().wrapContentHeight(), textAlign = TextAlign.Justify)
        Column {
            Row(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp, vertical = 10.dp),
                horizontalArrangement = Arrangement.End,
            ) {
                IconButton(
                    onClick = {
                        clipBoardManager.setText(AnnotatedString(("$title:\n$content") ?: ""))
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

@Composable
@Preview(showSystemUi = true)
fun PostDescriptionPreview() {
    val id = "123"
    PostDescription(id)
}