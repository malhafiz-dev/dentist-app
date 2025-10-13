package com.hafiz.dentistapp.network

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {

    // PENTING: Ganti dengan alamat IP server Anda!
    private const val BASE_URL = "http://10.194.82.186:8080/API_Dentist_App/"

    // URL Dasar untuk gambar. Pastikan path ini benar.
    const val IMAGE_BASE_URL = BASE_URL

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val client = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .build()

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val userService: UserService = retrofit.create(UserService::class.java)
}
