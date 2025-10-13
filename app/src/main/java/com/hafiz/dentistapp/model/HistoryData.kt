package com.hafiz.dentistapp.model

import com.google.gson.annotations.SerializedName

// Model data ini disesuaikan dengan respons API yang sebenarnya
data class HistoryData(
    @SerializedName("id")
    val id: Int,

    @SerializedName("user_id")
    val userId: Int,

    @SerializedName("type")
    val condition: String,

    // Diperbarui agar cocok dengan kolom 'image_path' dari API
    @SerializedName("image_path")
    val imagePath: String,

    // Diperbarui agar cocok dengan kolom 'timestamp' dari API
    @SerializedName("timestamp")
    val timestamp: String
)
