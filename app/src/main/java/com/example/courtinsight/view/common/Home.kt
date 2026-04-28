package com.example.courtinsight.view.common

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.courtinsight.R
import com.example.courtinsight.model.CarouselItem
import com.example.courtinsight.viewModel.CourtViewModel
import kotlinx.coroutines.delay

@Composable
fun Home(navController: NavController, viewModel: CourtViewModel = viewModel()) {
    LaunchedEffect(Unit) {
        viewModel.getAllPoster()
    }
    val posters = viewModel.posts.sortedByDescending { it.date }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                Carousel()
            }
            if (posters.isEmpty()){
                item {
                    Text("No Post Available", modifier = Modifier.padding(16.dp).fillMaxWidth(),
                        textAlign = TextAlign.Center, color = Color.DarkGray)
                }
            } else {
                items(posters) { post ->
                    homeCard(
                        id = post.id,
                        image = post.image,
                        title = post.title,
                        date = post.date,
                        content = post.what_happened,
                        navController = navController,
                    )
                }
            }
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Carousel() {

    val items = listOf(
        CarouselItem(1, R.drawable.caraousal_1, "CourtInsight"),
        CarouselItem(2, R.drawable.caraousal_2, "CourtInsight"),
        CarouselItem(3, R.drawable.caraousal_3, "CourtInsight")
    )

    val listState = rememberLazyListState()
    var currentindex = remember { 0 }
    LaunchedEffect(Unit) {
        while (true){
            delay(3000)
            currentindex = (currentindex + 1) % items.size
            listState.animateScrollToItem(currentindex)
        }
    }

    LazyRow(
        state = listState,
        contentPadding = PaddingValues(horizontal = 16.dp),
        modifier = Modifier
            .fillMaxWidth()
            .height(150.dp)
            .padding(top = 8.dp)
    ) {
        items(items.size) { index ->
            Image(
                painter = painterResource(id = items[index].imageResId),
                contentDescription = "CourtInsight",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .width(380.dp)
                    .height(150.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .padding(end = 12.dp)
            )

        }
    }
}

@Composable
@Preview(showSystemUi = true)
fun HomePreview(){
    Home(navController = rememberNavController())
}