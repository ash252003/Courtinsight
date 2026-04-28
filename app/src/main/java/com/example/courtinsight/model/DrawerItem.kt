package com.example.courtinsight.model

import androidx.annotation.DrawableRes
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

data class DrawerItem(
    val title: String,
    val route: String,
    val icon: ImageVector
)

data class CarouselItem(
    val id: Int,
    @DrawableRes val imageResId: Int,
    val contentDescription: String
)

data class SettingItem(
    val id: Int,
    val title: String,
    val imageResId: ImageVector,
    val contentDescription: String,
    val color: Color,
    val route: String
)

data class socialLogo(
    val id: Int,
    @DrawableRes val imageResId: Int,
    val contentDescription: String,
    val url: String
)

data class ChatMessage(
    val role: String,
    val text: String
)

data class SummaryDetails(
    val date: String,
    val title: String,
    val what_happened: String,
    val court_said: String,
    val court_update: String,
    val image: String
)

data class summaryPoster(
    val id: String,
    val date: String,
    val image: String,
    val title: String,
    val what_happened: String
)

data class userDetails(
    val name: String,
    val email: String,
    val gender: String,
    val user_type: String,
    val password: String
)