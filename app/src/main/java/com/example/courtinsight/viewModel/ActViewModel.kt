package com.example.courtinsight.viewModel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.courtinsight.model.ChatMessage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import okhttp3.Call
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException

class ActViewModel() : ViewModel() {
    var input by mutableStateOf("")
    var messages by mutableStateOf(listOf<ChatMessage>())
    var isTyping by mutableStateOf(false)

    private val client = OkHttpClient.Builder()
        .connectTimeout(120, java.util.concurrent.TimeUnit.SECONDS)
        .readTimeout(120, java.util.concurrent.TimeUnit.SECONDS)
        .build()

    fun sendMessage() {
        val question = input.trim()
        if (question.isEmpty()) return

        messages = messages + ChatMessage("user", question)
        input = ""
        isTyping = true

        askAI(question) { result ->
            viewModelScope.launch(Dispatchers.Main) {
                isTyping = false
                messages = messages + ChatMessage("ai", "")
                val aiIndex = messages.lastIndex
                wordByWord(result) { animatedText ->
                    val updated = messages.toMutableList()
                    updated[aiIndex] = ChatMessage("ai", animatedText)
                    messages = updated
                }
            }
        }
    }

    fun askAI(question: String, callback: (String) -> Unit) {

        val body = JSONObject().apply {
            put("data", JSONArray().apply {
                put(question)
                put(JSONObject.NULL)
            })
        }.toString()

        val postRequest = Request.Builder()
            .url("https://Ayushi054-BNS-Legal-Chat-v2.hf.space/gradio_api/call/chat")
            .addHeader("Content-Type", "application/json")
            .post(body.toRequestBody("application/json".toMediaType()))
            .build()

        client.newCall(postRequest).enqueue(object : okhttp3.Callback {
            override fun onFailure(call: Call, e: IOException) {
                callback("Network error: ${e.message}")
            }

            override fun onResponse(call: Call, response: Response) {
                val responseBody = response.body?.string() ?: ""
                android.util.Log.d("HF_POST", responseBody)

                try {
                    val eventId = JSONObject(responseBody).getString("event_id")

                    viewModelScope.launch(Dispatchers.IO) {
                        try {
                            val streamClient = OkHttpClient.Builder()
                                .connectTimeout(30, java.util.concurrent.TimeUnit.SECONDS)
                                .readTimeout(120, java.util.concurrent.TimeUnit.SECONDS)
                                .build()

                            val getRequest = Request.Builder()
                                .url("https://Ayushi054-BNS-Legal-Chat-v2.hf.space/gradio_api/call/chat/$eventId")
                                .addHeader("Accept", "text/event-stream")
                                .build()

                            streamClient.newCall(getRequest).execute().use { streamResponse ->
                                val source = streamResponse.body?.source()

                                while (true) {
                                    val line = source?.readUtf8Line() ?: break
                                    android.util.Log.d("HF_LINE", line)

                                    if (line.startsWith("event:") && line.contains("complete")) {
                                        // ✅ read the data line that immediately follows
                                        val dataLine = source?.readUtf8Line() ?: ""
                                        android.util.Log.d("HF_LINE", dataLine)

                                        val data = dataLine.removePrefix("data:").trim()
                                        if (data.isNotEmpty()) {
                                            try {
                                                val answer = JSONArray(data).getString(0)
                                                callback(answer)
                                            } catch (e: Exception) {
                                                callback("Parse error: ${e.message}")
                                            }
                                        } else {
                                            callback("No response received")
                                        }
                                        return@launch
                                    }

                                    if (line.startsWith("event:") && line.contains("error")) {
                                        callback("Error from server — please try again")
                                        return@launch
                                    }
                                }
                                callback("No response received")
                            }
                        } catch (e: Exception) {
                            callback("Stream error: ${e.message}")
                        }
                    }
                } catch (e: Exception) {
                    callback("Failed to get event_id: ${e.message}")
                }
            }
        })
    }

    private fun wordByWord(fullText: String, onUpdate: (String) -> Unit) {
        val words = fullText.split(Regex("\\s+"))
        val builder = StringBuilder()
        viewModelScope.launch(Dispatchers.Main) {
            for (word in words) {
                builder.append(word).append(" ")
                onUpdate(builder.toString().trim())
                delay(70)
            }
        }
    }

    fun clearChat() {
        messages = emptyList()
        input = ""
        isTyping = false
    }
}