package com.example.courtinsight.viewModel

import android.util.Log
import android.util.Patterns
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.courtinsight.model.UserModel
import com.example.courtinsight.model.userDetails
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import java.util.Properties
import javax.mail.Authenticator
import javax.mail.Message
import javax.mail.PasswordAuthentication
import javax.mail.Session
import javax.mail.Transport
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeMessage

class AuthViewModel(private val repo: UserModel = UserModel()): ViewModel() {

    private val _toastMessage = MutableSharedFlow<String>()
    val toastMessage = _toastMessage.asSharedFlow()
    var isRegistrationLoading by mutableStateOf(false)
    private set
    var name by mutableStateOf<String?>(null)
        private set
    var isLoginLoading by mutableStateOf(false)
    private set

    var isOtpLoading by mutableStateOf(false)
        private set

    var userLoading by mutableStateOf(false)
        private set
    var details by mutableStateOf<userDetails?>(null)

    fun isValidEmail(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
    fun isValidPassword(password: String): Boolean {
        return password.length >= 8 && password.any { it.isUpperCase() } && password.any { it.isDigit() }
    }
    fun isValidName(name: String): Boolean {
        return name.length >= 3
    }
    fun isValidConfirmPassword(password: String, confirmPassword: String): Boolean {
        return confirmPassword.isNotEmpty() && confirmPassword.equals(password)
    }

    fun isValidGender(gender: String) : Boolean {
        return gender != "Select Gender"
    }

    fun addUser(
        name: String,
        email: String,
        password: String,
        gender: String,
        onSuccess: () -> Unit
    ){
        viewModelScope.launch {
            isRegistrationLoading = true
            val result = repo.addUser(name, email, password, gender)
            isRegistrationLoading = false
            if(result){
                onSuccess()
            } else {
                _toastMessage.emit("Registration Failed")
            }
        }
    }
    fun checkLogin(
        email: String,
        password: String,
        onSuccess: (String?) -> Unit
    ){
        viewModelScope.launch {
            isLoginLoading = true
            val result = repo.checkLogin(email, password)
            isLoginLoading = false
            if(result != null){
                _toastMessage.emit("Login Successful")
                onSuccess(result)
            }else{
                _toastMessage.emit("Login Failed")
            }
        }
    }

    fun getName(email: String, onResult: (String?) -> Unit) {
        viewModelScope.launch {
            name = repo.getName(email)
            onResult(name)
        }
    }

    fun getGender(email: String, onResult: (String?) -> Unit) {
        viewModelScope.launch {
            onResult(repo.getGender(email))
        }
    }

    fun getUserDetails(email: String) {
        viewModelScope.launch {
            userLoading = true
            details = repo.getAllDetails(email)
            userLoading = false
        }
    }

    fun updateDetails(email: String, name: String, gender: String, password: String, onSuccess: () -> Unit) {
        viewModelScope.launch {
            val result = repo.updateDetails(email, name, gender, password)
            if(result){
                onSuccess()
            } else {
                _toastMessage.emit("Update Failed")
            }
        }
    }

    fun checkEmail(email: String, onResult: (Boolean) -> Unit){
        viewModelScope.launch {
            onResult(repo.checkEmail(email))
        }
    }

    suspend fun sendEmail(email: String): String? {
        return withContext(Dispatchers.IO) {
            try {

                val otp = generateOtp()
                val json = """
                {
                    "email": "$email",
                    "subject": "OTP Verification",
                    "message": "Your OTP is: $otp"
                }
                """.trimIndent()

                val body = json.toRequestBody("application/json".toMediaType())

                val request = Request.Builder()
                    .url("https://courtinsight-email-api.onrender.com/send-email")
                    .post(body)
                    .build()

                val client = OkHttpClient()
                val response = client.newCall(request).execute()

                response.body?.string()

            } catch (e: Exception) {
                Log.e("Error", "Error sending email: ${e.message}")
                null
            }
        }
    }

    fun generateOtp(): String{
        val otp = (100000..999999).random().toString()
        return otp
    }

    fun sendEmailScope(email: String, onResult: (String?) -> Unit){
        viewModelScope.launch {
            isOtpLoading = true
            val otp = sendEmail(email)
            isOtpLoading = false
            onResult(otp)
        }
    }

    fun updatePassword(email: String, password: String, onSuccess: () -> Unit){
        viewModelScope.launch {
            val result = repo.updatePassword(email, password)
            if(result){
                onSuccess()
            } else {
                _toastMessage.emit("Something went wrong! Please try again later")
            }
        }
    }

    fun getAdminKey(onSuccess: (String) -> Unit){
        viewModelScope.launch {
            userLoading = true
            val adminPIn = repo.getAdminPIN() ?: ""
            userLoading = false
            onSuccess(adminPIn)
        }
    }

    fun updateUserType(email: String, userType: String, onSuccess: () -> Unit){
        viewModelScope.launch {
            val result = repo.updateUserType(email, userType)
            if(result){
                onSuccess()
            } else {
                _toastMessage.emit("Something went wrong! Please try again later")
            }
        }
    }
}