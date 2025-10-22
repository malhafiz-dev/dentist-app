package com.hafiz.dentistapp.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.hafiz.dentistapp.data.ChatRepository

/**
 * Factory untuk membuat instance ChatViewModel dengan ChatRepository sebagai dependency.
 */
class ChatViewModelFactory(private val repository: ChatRepository) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ChatViewModel::class.java)) {
            return ChatViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
