package com.hafiz.dentistapp.model

import com.google.gson.annotations.SerializedName

// Menggunakan Generik <T> sama seperti di versi Java.
data class BaseResponse<T>(
    @SerializedName("status")
    val status: String?, // "success" atau "error"

    @SerializedName("message")
    val message: String?,

    @SerializedName("data")
    val data: T? // Data generik, bisa berupa UserData atau list data lainnya
)
