package com.hafiz.dentistapp.model

import com.google.gson.annotations.SerializedName

data class ChatbotResponse(
    @SerializedName("response")
    val response: String
)
