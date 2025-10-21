package com.hafiz.dentistapp.model

import com.google.gson.annotations.SerializedName

data class ChatbotRequest(
    @SerializedName("message")
    val message: String
)
