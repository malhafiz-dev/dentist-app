package com.hafiz.dentistapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.hafiz.dentistapp.model.ChatMessage

class ChatAdapter(private val chatMessages: MutableList<ChatMessage>) : RecyclerView.Adapter<ChatAdapter.MessageViewHolder>() {

    companion object {
        private const val VIEW_TYPE_USER = 1
        private const val VIEW_TYPE_BOT = 2
    }

    /**
     * Fungsi baru untuk menambahkan pesan dan memperbarui UI.
     * Ini memastikan bahwa daftar yang diubah adalah daftar yang sama dengan yang digunakan adapter.
     */
    fun addMessage(chatMessage: ChatMessage) {
        chatMessages.add(chatMessage)
        notifyItemInserted(chatMessages.size - 1)
    }

    override fun getItemViewType(position: Int): Int {
        return if (chatMessages[position].sender == ChatMessage.Sender.USER) {
            VIEW_TYPE_USER
        } else {
            VIEW_TYPE_BOT
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = if (viewType == VIEW_TYPE_USER) {
            layoutInflater.inflate(R.layout.item_chat_message_user, parent, false)
        } else {
            layoutInflater.inflate(R.layout.item_chat_message_bot, parent, false)
        }
        return MessageViewHolder(view)
    }

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        val chatMessage = chatMessages[position]
        holder.bind(chatMessage)
    }

    override fun getItemCount(): Int {
        return chatMessages.size
    }

    inner class MessageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val textViewMessage: TextView = itemView.findViewById(R.id.textViewMessage)

        fun bind(chatMessage: ChatMessage) {
            textViewMessage.text = chatMessage.text
        }
    }
}
