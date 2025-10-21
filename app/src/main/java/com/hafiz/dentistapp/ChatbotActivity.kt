package com.hafiz.dentistapp

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.hafiz.dentistapp.data.ChatRepository
import com.hafiz.dentistapp.databinding.ActivityChatbotBinding
import com.hafiz.dentistapp.model.ChatMessage
import com.hafiz.dentistapp.ui.ChatViewModel
import com.hafiz.dentistapp.ui.ChatViewModelFactory

class ChatbotActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChatbotBinding
    private lateinit var chatAdapter: ChatAdapter
    // Daftar ini hanya digunakan untuk inisialisasi awal adapter.
    private val initialMessages = mutableListOf<ChatMessage>()

    private val viewModel: ChatViewModel by viewModels {
        ChatViewModelFactory(ChatRepository())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatbotBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()
        setupObservers()

        binding.backButton.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        binding.buttonSend.setOnClickListener {
            handleSendMessage()
        }

        // Add initial greeting from the bot
        addBotMessage("Hello! I'm Tootie, your dental health assistant. How can I help you today?")
    }

    private fun setupRecyclerView() {
        // Adapter sekarang mengelola daftarnya sendiri setelah inisialisasi.
        chatAdapter = ChatAdapter(initialMessages)
        binding.recyclerViewChat.apply {
            adapter = chatAdapter
            layoutManager = LinearLayoutManager(this@ChatbotActivity)
        }
    }

    private fun setupObservers() {
        viewModel.chatbotResponse.observe(this, Observer { result ->
            result.fold(
                onSuccess = {
                    addBotMessage(it.response)
                },
                onFailure = { error ->
                    addBotMessage("Sorry, something went wrong. Please check your connection or try again later.")
                    Toast.makeText(this, "Error: ${error.message}", Toast.LENGTH_SHORT).show()
                }
            )
        })
    }

    private fun handleSendMessage() {
        val messageText = binding.editTextMessage.text.toString().trim()
        if (messageText.isNotEmpty()) {
            val userMessage = ChatMessage(messageText, ChatMessage.Sender.USER)
            addMessageToChat(userMessage)

            binding.editTextMessage.text.clear()

            viewModel.sendMessage(messageText)
        }
    }

    private fun addBotMessage(messageText: String) {
        val botMessage = ChatMessage(messageText, ChatMessage.Sender.BOT)
        addMessageToChat(botMessage)
    }

    /**
     * Memanggil fungsi di adapter untuk menambahkan pesan.
     * Ini adalah cara yang benar untuk memastikan UI diperbarui.
     */
    private fun addMessageToChat(message: ChatMessage) {
        chatAdapter.addMessage(message)
        // Selalu gulir ke item terakhir di adapter
        binding.recyclerViewChat.scrollToPosition(chatAdapter.itemCount - 1)
    }
}
