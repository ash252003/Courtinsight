package com.example.courtinsight.viewModel

import androidx.compose.runtime.MutableState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.courtinsight.model.UserModel
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.example.courtinsight.model.userDetails
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

class UserViewModel(
    private val repo: UserModel = UserModel()
) : ViewModel() {

    var feedBackLoading by mutableStateOf(false)
        private set

    private val _toastMessage = MutableSharedFlow<String>()
    val toastMessage = _toastMessage.asSharedFlow()
    fun validateFeedback(feedback: String): Boolean {
        return feedback.isNotEmpty()
    }

    fun addFeedBack(email: String, feedback: String, rating: Int) {
        viewModelScope.launch {
            feedBackLoading = true
            try {
                repo.addFeedBack(email, rating, feedback)
                _toastMessage.emit("Feedback Submitted")
            } catch (e: Exception) {
                _toastMessage.emit("Failed to submit feedback")
            }
            feedBackLoading = false
        }
    }
}