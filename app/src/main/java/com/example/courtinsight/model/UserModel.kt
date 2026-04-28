package com.example.courtinsight.model

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class UserModel {
    val firestore = FirebaseFirestore.getInstance()

    //User Registration
    suspend fun addUser(name: String, email: String, password: String, gender: String): Boolean{
        try {
            val user = mapOf(
                "name" to name,
                "email" to email,
                "password" to password,
                "gender" to gender,
                "user_type" to "user"
            )
            val collectionRef = firestore.collection("users")
                .document()
            collectionRef.set(user).await()
            return true
        } catch (e: Exception){
            Log.e("Error", "Error adding User: ${e.message}")
            return false
        }
    }

    //Login
    suspend fun checkLogin(email: String, password: String): String?{
        try {
            val userRef = firestore.collection("users")
                .whereEqualTo("email", email)
                .whereEqualTo("password", password)
                .get()
                .await()
            if(!userRef.isEmpty){
                val document = userRef.documents[0]
                val userType = document.getString("user_type")
                return userType
            }
        } catch (e: Exception){
            Log.e("Error", "Error checking login: ${e.message}")
        }
        return null
    }

    //Feedback
    suspend fun addFeedBack(email: String, rating: Int, feedback: String): Boolean{
        try {
            val feedback = mapOf(
                "email" to email,
                "rating" to rating,
                "feedback" to feedback,
            )
            val collectionRef = firestore.collection("feedback")
                .document()
            collectionRef.set(feedback).await()
            return true
        } catch (e: Exception){
            Log.e("Error", "Error adding User: ${e.message}")
            return false
        }
    }

    suspend fun getName(email: String): String? {
        try {
            val userRef = firestore.collection("users")
                .whereEqualTo("email", email)
                .get()
                .await()
            if (!userRef.isEmpty){
                val document = userRef.documents[0]
                val name = document.getString("name")
                return name
            }
        } catch (e: Exception){
            Log.e("Error", "Error getting name: ${e.message}")
        }
        return null
    }

    suspend fun getGender(email: String): String? {
        try {
            val userRef = firestore.collection("users")
                .whereEqualTo("email", email)
                .get()
                .await()
            if (!userRef.isEmpty){
                val document = userRef.documents[0]
                val name = document.getString("gender")
                return name
            }
        } catch (e: Exception){
            Log.e("Error", "Error getting gender: ${e.message}")
        }
        return null
    }

    suspend fun getAllDetails(email: String): userDetails?{
        try {
            val userRef = firestore.collection("users")
                .whereEqualTo("email", email)
                .get()
                .await()
            if (!userRef.isEmpty){
                val document = userRef.documents[0]
                val name = document.getString("name")
                val gender = document.getString("gender")
                val userType = document.getString("user_type")
                val password = document.getString("password")
                return userDetails(name.toString(), email, gender.toString(), userType.toString(), password.toString())
            }
        } catch (e: Exception){
            Log.e("Error", "Error getting all details: ${e.message}")
        }
        return null
    }

    suspend fun updateDetails(email: String, name: String, gender: String, password: String): Boolean{
        try {
            val userRef = firestore.collection("users")
                .whereEqualTo("email", email)
                .get()
                .await()
            if (!userRef.isEmpty){
                val docId = userRef.documents[0].id
                val user = mapOf(
                    "name" to name,
                    "gender" to gender,
                    "password" to password
                )
                firestore.collection("users").document(docId).update(user).await()
                return true
            }
        } catch (e: Exception){
            Log.e("Error", "Error updating details: ${e.message}")
        }
        return false
    }

    suspend fun checkEmail(email: String): Boolean{
        try {
            val userRef = firestore.collection("users")
                .whereEqualTo("email", email)
                .get()
                .await()
            if(!userRef.isEmpty){
                return true
            }
        } catch (e: Exception){
            Log.e("Error", "Error checking email: ${e.message}")
        }
        return false
    }

    suspend fun updatePassword(email: String, password: String): Boolean{
        try {
            val userRef = firestore.collection("users")
                .whereEqualTo("email", email)
                .get()
                .await()
            if(!userRef.isEmpty){
                val docId = userRef.documents[0].id
                val user = mapOf(
                    "password" to password
                )
                firestore.collection("users").document(docId).update(user).await()
                return true
            }
        } catch (e: Exception){
            Log.e("Error", "Error updating password: ${e.message}")
        }
        return false
    }

    suspend fun getAdminPIN(): String?{
        try {
            val adminKey = firestore.collection("admin_key")
                .get()
                .await()
            if(!adminKey.isEmpty){
                val document = adminKey.documents[0]
                val pin = document.getString("key")
                return pin
            }
        } catch (e: Exception){
            Log.e("Error", "Error getting admin PIN: ${e.message}")
        }
        return null
    }

    suspend fun updateUserType(email: String, userType: String): Boolean{
        try {
            val userRef = firestore.collection("users")
                .whereEqualTo("email", email)
                .get()
                .await()
            if(!userRef.isEmpty){
                val docId = userRef.documents[0].id
                val user = mapOf(
                    "user_type" to userType
                )
                firestore.collection("users").document(docId).update(user).await()
                return true
            }
        } catch (e: Exception){
            Log.e("Error", "Error updating password: ${e.message}")
        }
        return false
    }
}