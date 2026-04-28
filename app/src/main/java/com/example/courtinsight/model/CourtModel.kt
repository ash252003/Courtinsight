package com.example.courtinsight.model

import android.annotation.SuppressLint
import android.icu.util.Calendar
import android.icu.util.LocaleData
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Date

class CourtModel {
    val firestore = FirebaseFirestore.getInstance()
    @SuppressLint("SimpleDateFormat")
    suspend fun addPost(title: String, what_happened: String, caseDetails: String, courtDecision: String, image: String){
        val dateFormat = SimpleDateFormat("dd/M/yyyy")
        val date = dateFormat.format(Date())
        try {
            val fileDate = mapOf(
                "title" to title,
                "date" to date,
                "what_happened" to what_happened,
                "case_details" to caseDetails,
                "court_decision" to courtDecision,
                "image" to image
            )
            val collectionRef = firestore.collection("summaries")
            collectionRef.add(fileDate).await()
        } catch (e: Exception){
            Log.e("Error", "Error adding File Data: ${e.message}")
        }
    }

    suspend fun getPostDetails(id: String): SummaryDetails?{
        return try {
            val document = firestore.collection("summaries")
                .document(id)
                .get()
                .await()
            if (document.exists()) {
                val date = document.getString("date") ?: ""
                val title = document.getString("title") ?: ""
                val what_happened = document.getString("what_happened") ?: ""
                val court_said = document.getString("case_details") ?: ""
                val court_update = document.getString("court_decision") ?: ""
                val image = document.getString("image") ?: ""
                SummaryDetails(date, title, what_happened, court_said, court_update, image)
            } else {
                null
            }
        } catch (e: Exception){
            Log.e("Error", "Error getting File Data: ${e.message}")
            null
        }
    }

    suspend fun getAllPoster(): List<summaryPoster>{
        val summaries = mutableListOf<summaryPoster>()
        try {
            val collectionRef = firestore.collection("summaries")
            val querySnapshot = collectionRef.get().await()
            for (document in querySnapshot.documents){
                val id = document.id
                val title = document.getString("title") ?: ""
                val date = document.getString("date") ?: ""
                val what_happened = document.getString("what_happened") ?: ""
                val image = document.getString("image") ?: ""
                val summary = summaryPoster(id, date, image, title, what_happened)
                summaries.add(summary)
            }
            return summaries
        } catch (e: Exception){
            Log.e("Error", "Error getting File Data: ${e.message}")
            return emptyList()
        }
    }

    suspend fun deletePost(id: String){
        try {
            val document = firestore.collection("summaries")
                .document(id)
                .delete()
                .await()
        } catch (e: Exception){
            Log.e("Error", "Error deleting File Data: ${e.message}")
        }
    }

}