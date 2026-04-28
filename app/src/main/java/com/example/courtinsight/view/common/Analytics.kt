package com.example.courtinsight.view.common

import android.graphics.Bitmap
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLinkStyles
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withLink
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView

@Composable
fun Analytics() {
    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {
        item {
            Row(modifier = Modifier.fillMaxWidth()
                .height(40.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically) {
                Text(
                    buildAnnotatedString {
                        append("For More Information Visit: ")
                        withLink(
                            LinkAnnotation.Url(
                                url = "https://njdg.ecourts.gov.in/",
                                styles = TextLinkStyles(
                                    style = SpanStyle(
                                        color = Color.Blue,
                                        fontWeight = FontWeight.Bold
                                    )
                                )
                            )
                        ) {
                            append("NJDG")
                        }
                    }
                )
            }
            NJDGWebView()
            Row(modifier = Modifier.fillMaxWidth()
                .wrapContentHeight().padding(10.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically) {
                Text("Note: All the information displayed here is authentic and is part of NJDG official website",
                    textAlign = TextAlign.Center)
            }
        }
    }
}

@Composable
fun NJDGWebView() {
    val context = LocalContext.current
    val webView = remember { WebView(context) }
    var isLoading by remember { mutableStateOf(true) }
    Box(modifier = Modifier.fillMaxSize()){
        AndroidView(
            modifier = if (isLoading){
                Modifier.height(0.dp)
            }else {
                Modifier.fillMaxSize()
            },
            factory = { webView },
            update = {
                it.settings.javaScriptEnabled = true
                it.settings.domStorageEnabled = true
                it.webViewClient = object : WebViewClient() {
                    override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                        view?.evaluateJavascript(
                            """
                                (function() {
                                    var style = document.createElement('style');
                                    style.innerHTML = `
                                        .navbar, .nav, .alerts, footer, .menu {
                                            display: none !important;
                                        }
                                        body {
                                            margin:0 !important;
                                            padding:0 !important;
                                        }
                                    `;
                                    document.head.appendChild(style);
                                })();
                                """.trimIndent(),
                            null
                        )
                        isLoading = true
                    }
                    override fun onPageFinished(view: WebView?, url: String?) {
                        super.onPageFinished(view, url)
                        isLoading = false
                        view?.evaluateJavascript(
                            """
                                (function() {
                                    var style = document.createElement('style');
                                    style.innerHTML = `
                                        .navbar, .nav, .alerts, footer, .menu {
                                            display: none !important;
                                        }
                                        body {
                                            margin:0 !important;
                                            padding:0 !important;
                                        }
                                    `;
                                    document.head.appendChild(style);
                                })();
                                """.trimIndent(),
                            null
                        )
                    }
                }

                if (it.url == null) {
                    it.loadUrl("https://njdg.ecourts.gov.in/")
                }
            }
        )
        if (isLoading){
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center)
            )
        }
    }
}