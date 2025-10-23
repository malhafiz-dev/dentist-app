package com.hafiz.dentistapp.network

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {

    // --- Konfigurasi API yang sudah ada ---
    private const val BASE_URL = "http://10.194.82.186:8080/API_Dentist_App/"
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


    // --- Konfigurasi BARU untuk API Chatbot ---

    // PENTING: Pastikan alamat IP ini adalah alamat IP lokal komputer Anda
    private const val CHATBOT_BASE_URL = "http://10.194.82.186:5000/"

    // Kita bisa menggunakan logging interceptor yang sama
    private val chatClient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .build()

    private val chatRetrofit = Retrofit.Builder()
        .baseUrl(CHATBOT_BASE_URL)
        .client(chatClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    // Instance service yang akan dipanggil dari repository
    val chatApiService: ChatApiService by lazy {
        chatRetrofit.create(ChatApiService::class.java)
    }
}
