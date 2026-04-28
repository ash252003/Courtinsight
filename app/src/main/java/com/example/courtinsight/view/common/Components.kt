package com.example.courtinsight.view.common

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.courtinsight.R
import com.example.courtinsight.viewModel.CourtViewModel

@Composable
fun CourtPreview(id: String, image: String, title: String, date: String, content: String, navController: NavController, viewModel: CourtViewModel){
    var expanded by remember { mutableStateOf(false) }
    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 5.dp, end = 3.dp),
            horizontalArrangement = Arrangement.End,
        ) {

            IconButton(onClick = { expanded = !expanded }) {
                Icon(
                    imageVector = Icons.Default.MoreVert,
                    contentDescription = "More Button"
                )
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    DropdownMenuItem(
                        text = { Text("Delete") },
                        onClick = {
                            expanded = false
                            viewModel.deletePost(id)
                        }
                    )
                }
            }
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(
                    start = 16.dp,
                    end = 16.dp,
                    bottom = 16.dp
                ),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AsyncImage(
                model = image,
                contentDescription = "Post",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp),
                contentScale = ContentScale.Crop
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                horizontalArrangement = Arrangement.End
            ) {
                Text(
                    date,
                    fontWeight = FontWeight.Bold,
                    color = colorResource(R.color.app_main)
                )
            }

            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    title,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(top = 8.dp),
                    maxLines = 2
                )
                Text(
                    content,
                    textAlign = TextAlign.Justify,
                    modifier = Modifier.padding(top = 8.dp),
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp),
                horizontalArrangement = Arrangement.End
            ) {
                TextButton(
                    onClick = {
                        navController.navigate("post_description/$id")
                    },
                ) {
                    Text(
                        "View more >>",
                        fontWeight = FontWeight.Bold,
                        color = colorResource(R.color.app_main),
                    )
                }
            }
        }
    }
}

@Composable
fun homeCard(
    id: String = "",
    image: String,
    title: String,
    date: String,
    content: String,
    navController: NavController
){
    ElevatedCard(modifier = Modifier.fillMaxWidth()
        .wrapContentHeight()
        .padding(16.dp),) {
        Column(modifier = Modifier.fillMaxSize().padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center) {
            AsyncImage(
                model = image,
                contentDescription = "Post",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp),
                contentScale = ContentScale.Crop
            )
            Row(modifier = Modifier.fillMaxWidth().padding(top = 8.dp), horizontalArrangement = Arrangement.End,
            ) {
                Text(date, fontWeight = FontWeight.Bold, color = colorResource(R.color.app_main))
            }
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(title, fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(top = 8.dp), maxLines = 2)
                Text(content,
                    textAlign = TextAlign.Justify, modifier = Modifier.padding(top = 8.dp), maxLines = 3, overflow = TextOverflow.Ellipsis)
            }
            Row(modifier = Modifier.fillMaxWidth().padding(top = 10.dp), horizontalArrangement = Arrangement.End) {
                Text("View more >>", fontWeight = FontWeight.Bold, color = colorResource(R.color.app_main),
                    modifier = Modifier.clickable{
                        navController.navigate("post_description/$id")
                    }
                )
            }
        }
    }
}
