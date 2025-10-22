package com.hafiz.dentistapp.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hafiz.dentistapp.data.ChatRepository
import com.hafiz.dentistapp.model.ChatbotResponse
import kotlinx.coroutines.launch

class ChatViewModel(private val repository: ChatRepository) : ViewModel() {

    // LiveData untuk menampung hasil dari API
    private val _chatbotResponse = MutableLiveData<Result<ChatbotResponse>>()
    val chatbotResponse: LiveData<Result<ChatbotResponse>> = _chatbotResponse

    /**
     * Mengirim pesan ke repository di dalam coroutine yang aman (viewModelScope).
     */
    fun sendMessage(message: String) {
        viewModelScope.launch {
            val result = repository.sendMessage(message)
            _chatbotResponse.postValue(result)
        }
    }
}
