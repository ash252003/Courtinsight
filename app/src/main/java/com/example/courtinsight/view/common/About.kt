package com.example.courtinsight.view.common

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastForEachIndexed
import com.example.courtinsight.R
import com.example.courtinsight.model.socialLogo
import androidx.core.net.toUri

@Composable
fun About() {
    val version = "1.0.0"
    val terms = stringResource(R.string.terms)
    val privacyPolicy = stringResource(R.string.privacy)
    val label = listOf("App Version", "Develop By", "Terms & Conditions", "Privacy Policy")
    val labelValue = listOf(version, "CourtInsight Team", terms, privacyPolicy)
    val item = listOf(
        socialLogo(1, R.drawable.facebook, "Facebook",
            stringResource(R.string.facebok_link)
        ),
        socialLogo(2, R.drawable.instagram, "Instagram",
            stringResource(R.string.insta_link)
        ),
        socialLogo(3, R.drawable.linkedin, "Linkedin",
            stringResource(R.string.linkedin_link)
        ),
    )
    val context = LocalContext.current
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        item {
            Image(
                painter = painterResource(R.drawable.ci_logo),
                contentDescription = "Logo",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(140.dp)
            )
            Column(modifier = Modifier
                .wrapContentHeight()
                .width(300.dp),
                verticalArrangement = Arrangement.Center) {
                Column(modifier = Modifier.padding(top = 10.dp)) {
                    Text("About Us", fontWeight = FontWeight.Bold,
                        fontSize = MaterialTheme.typography.headlineSmall.fontSize, modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 10.dp))
                    Text(
                        "CourtInsight is a legal information platform designed to make court-related data easier to access and understand. The app provides organized case insights, legal explanations, and useful resources through a simple and user-friendly interface. Our goal is to help users quickly explore legal information and stay informed using modern mobile technology.",
                        fontSize = MaterialTheme.typography.titleMedium.fontSize, textAlign = TextAlign.Justify,
                    )
                }
                label.fastForEachIndexed { index, string ->
                    Column(modifier = Modifier.padding(top = 20.dp)) {
                        Row(modifier = Modifier.fillMaxWidth()) {
                            Text((index + 1).toString() + ".", fontWeight = FontWeight.Bold,
                                fontSize = MaterialTheme.typography.titleLarge.fontSize, modifier = Modifier.padding(end = 8.dp))
                            Text(string, fontWeight = FontWeight.Bold,
                                fontSize = MaterialTheme.typography.titleLarge.fontSize, textAlign = TextAlign.Justify)
                        }
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(labelValue[index], fontSize = MaterialTheme.typography.titleMedium.fontSize,
                                modifier = Modifier.padding(start = 30.dp))
                        }
                    }
                }
                Row(modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 20.dp),
                    horizontalArrangement = Arrangement.SpaceBetween) {
                    item.forEach { item ->
                        Image(
                            painter = painterResource(item.imageResId),
                            contentDescription = item.contentDescription,
                            modifier = Modifier
                                .size(50.dp)
                                .clickable {
                                    val intent = Intent(
                                        Intent.ACTION_VIEW,
                                        item.url.toUri()
                                    )
                                    context.startActivity(intent)
                                }
                        )
                    }
                }
                Text("Copyright © 2026 CourtInsight.", modifier = Modifier
                    .padding(top = 30.dp)
                    .fillMaxWidth(), textAlign = TextAlign.Center)
            }
        }
    }
}

@Composable
@Preview(showSystemUi = true)
fun AboutPreview() {
    About()
}