package com.example.courtinsight.viewModel

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.courtinsight.model.CourtModel
import com.example.courtinsight.model.SummaryDetails
import com.example.courtinsight.model.summaryPoster
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import okio.BufferedSink
import okio.source
import org.json.JSONObject
import java.io.IOException
import java.util.UUID
import java.util.concurrent.TimeUnit

class CourtViewModel(
    private val repo: CourtModel = CourtModel()
) : ViewModel() {

    var isUploading by mutableStateOf(false)
        private set

    var selectedPost by mutableStateOf<SummaryDetails?>(null)
        private set

    var isLoading by mutableStateOf(false)
        private set

    var posts by mutableStateOf<List<summaryPoster>>(emptyList())
        private set

    private val _toastMessage = MutableSharedFlow<String>()
    val toastMessage = _toastMessage.asSharedFlow()

    private val client = OkHttpClient.Builder()
        .connectTimeout(120, TimeUnit.SECONDS)
        .readTimeout(120, TimeUnit.SECONDS)
        .build()

    fun uploadPdf(context: Context, pdfUri: Uri) {

        isUploading = true

        val requestBody = object : RequestBody() {
            override fun contentType(): MediaType =
                "application/pdf".toMediaType()

            override fun writeTo(sink: BufferedSink) {
                context.contentResolver.openInputStream(pdfUri)?.use {
                    sink.writeAll(it.source())
                }
            }
        }

        val multipart = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart("files", "document.pdf", requestBody)
            .build()

        val uploadRequest = Request.Builder()
            .url("https://ayushi054-summaraization.hf.space/gradio_api/upload")
            .post(multipart)
            .build()

        client.newCall(uploadRequest).enqueue(object : Callback {

            override fun onFailure(call: Call, e: IOException) {
                viewModelScope.launch {
                    isUploading = false
                    _toastMessage.emit("Upload failed")
                }
            }

            override fun onResponse(call: Call, response: Response) {

                try {
                    val body = response.body?.string() ?: ""
                    Log.d("HF_UPLOAD", body)
                    val array = org.json.JSONArray(body)
                    val filePath = array.getString(0)
                    startQueue(filePath)
                } catch (e: Exception) {

                    viewModelScope.launch {
                        isUploading = false
                        _toastMessage.emit("Upload parsing failed")
                    }
                }
            }
        })
    }

    private fun startQueue(filePath: String) {

        val sessionHash = UUID.randomUUID().toString().replace("-", "").take(10)

        val jsonObject = JSONObject()

        val dataArray = org.json.JSONArray()

        val fileObj = JSONObject()
        fileObj.put("path", filePath)

        val meta = JSONObject()
        meta.put("_type", "gradio.FileData")

        fileObj.put("meta", meta)

        dataArray.put(fileObj)

        jsonObject.put("data", dataArray)
        jsonObject.put("fn_index", 0)
        jsonObject.put("session_hash", sessionHash)

        val request = Request.Builder()
            .url("https://ayushi054-summaraization.hf.space/gradio_api/queue/join")
            .addHeader("Content-Type", "application/json")
            .post(
                jsonObject.toString()
                    .toRequestBody("application/json".toMediaType())
            )
            .build()

        client.newCall(request).enqueue(object : Callback {

            override fun onFailure(call: Call, e: IOException) {

                viewModelScope.launch {
                    isUploading = false
                    _toastMessage.emit("Queue error")
                }
            }

            override fun onResponse(call: Call, response: Response) {

                Log.d("HF_JOIN", response.body?.string() ?: "")

                listenForResult(sessionHash)
            }
        })
    }

    private fun listenForResult(sessionHash: String) {

        viewModelScope.launch(Dispatchers.IO) {

            try {

                val request = Request.Builder()
                    .url("https://ayushi054-summaraization.hf.space/gradio_api/queue/data?session_hash=$sessionHash")
                    .addHeader("Accept", "text/event-stream")
                    .build()

                client.newCall(request).execute().use { response ->

                    val source = response.body?.source()

                    while (true) {

                        val line = source?.readUtf8Line() ?: break

                        Log.d("HF_STREAM", line)

                        if (line.startsWith("data:")) {

                            val json = line.removePrefix("data:").trim()

                            val obj = JSONObject(json)

                            if (obj.optString("msg") == "process_completed") {

                                val output = obj
                                    .getJSONObject("output")
                                    .getJSONArray("data")
                                    .getJSONObject(0)

                                val headline =
                                    output.optString("headline","")

                                val what =
                                    output.optString("what_happened","")

                                val details =
                                    output.optString("case_details","")

                                val decision =
                                    output.optString("court_decision","")

                                viewModelScope.launch {

                                    addSummary(
                                        headline,
                                        what,
                                        details,
                                        decision
                                    )

                                    isUploading = false

                                    _toastMessage.emit(
                                        "Summary generated successfully"
                                    )
                                }

                                return@launch
                            }
                        }
                    }
                }

            } catch (e: Exception) {

                viewModelScope.launch {
                    isUploading = false
                    _toastMessage.emit("Stream error")
                }
            }
        }
    }

    fun generateLegalImage(): String {
        val images = listOf(
            "https://images.unsplash.com/photo-1589829545856-d10d557cf95f",
            "https://images.unsplash.com/photo-1593115057322-e94b77572f20",
            "https://images.unsplash.com/photo-1450101499163-c8848c66ca85",
            "https://images.unsplash.com/photo-1505664194779-8beaceb93744",
        )
        return images.random() + "?auto=format&fit=crop&w=800&q=60"
    }

    private fun addSummary(
        title: String,
        whatHappened: String,
        caseDetails: String,
        courtDecision: String
    ) {
        val image = generateLegalImage()
        viewModelScope.launch {
            repo.addPost(
                title,
                whatHappened,
                caseDetails,
                courtDecision,
                image
            )
            getAllPoster()
        }
    }

    fun getPostById(id: String) {
        viewModelScope.launch {
            isLoading = true
            selectedPost = repo.getPostDetails(id)
            isLoading = false
        }
    }

    fun getAllPoster() {
        viewModelScope.launch {
            isLoading = true
            posts = repo.getAllPoster()
            isLoading = false
        }
    }

    fun deletePost(id: String){
        viewModelScope.launch {
            isLoading = true
            repo.deletePost(id)
            getAllPoster()
            isLoading = false
        }
    }
}