package com.hafiz.dentistapp.model

import com.google.gson.annotations.SerializedName

// Data class yang jauh lebih ringkas, menggantikan boilerplate Java.
data class UserData(
    @SerializedName("user_id")
    val userId: Int?, // Menggunakan tipe nullable (Int?) untuk keamanan

    @SerializedName("username")
    val username: String?,

    @SerializedName("email")
    val email: String?,

    @SerializedName("created_at")
    val createdAt: String?
)
