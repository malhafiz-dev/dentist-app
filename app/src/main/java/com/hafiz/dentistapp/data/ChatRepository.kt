package com.hafiz.dentistapp.data

import com.hafiz.dentistapp.network.ApiClient
import com.hafiz.dentistapp.model.ChatbotRequest
import com.hafiz.dentistapp.model.ChatbotResponse

class ChatRepository {

    /**
     * Mengirim pesan ke server chatbot dan mengembalikan hasilnya.
     * Menggunakan Result<T> untuk error handling yang aman.
     */
    suspend fun sendMessage(message: String): Result<ChatbotResponse> {
        return try {
            val request = ChatbotRequest(message = message)
            // Memanggil fungsi yang benar dari chatApiService
            val response = ApiClient.chatApiService.sendMessageToBot(request)
            Result.success(response)
        } catch (e: Exception) {
            // Menangkap semua jenis error (koneksi, timeout, dll.)
            Result.failure(e)
        }
    }
}
