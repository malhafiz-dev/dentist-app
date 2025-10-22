package com.hafiz.dentistapp.network

import com.hafiz.dentistapp.model.ChatbotRequest
import com.hafiz.dentistapp.model.ChatbotResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface ChatApiService {
    /**
     * Mengirim pesan ke chatbot.
     * Dibuat sebagai suspend function untuk integrasi langsung dengan Coroutines.
     * Retrofit akan menangani eksekusi di background thread.
     */
    @POST("predict")
    suspend fun sendMessageToBot(
        @Body request: ChatbotRequest
    ): ChatbotResponse // Langsung mengembalikan data, bukan Call<T>
}
