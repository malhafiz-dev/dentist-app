package com.hafiz.dentistapp.model

data class ChatMessage(
    val text: String,
    val sender: Sender
) {
    enum class Sender {
        USER, BOT
    }
}
