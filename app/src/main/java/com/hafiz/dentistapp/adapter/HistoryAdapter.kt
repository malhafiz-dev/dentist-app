package com.hafiz.dentistapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.hafiz.dentistapp.databinding.ItemHistoryBinding
import com.hafiz.dentistapp.model.HistoryData
import com.hafiz.dentistapp.network.ApiClient

class HistoryAdapter(
    private var historyList: List<HistoryData>,
    private val onItemClick: (HistoryData) -> Unit
) : RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        val binding = ItemHistoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HistoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        val historyItem = historyList[position]
        holder.bind(historyItem)
        holder.itemView.setOnClickListener {
            onItemClick(historyItem)
        }
    }

    override fun getItemCount(): Int = historyList.size

    fun updateData(newHistoryList: List<HistoryData>) {
        historyList = newHistoryList
        notifyDataSetChanged()
    }

    inner class HistoryViewHolder(private val binding: ItemHistoryBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(historyData: HistoryData) {
            binding.textViewDiseaseType.text = historyData.condition.capitalize()
            binding.textViewDate.text = historyData.timestamp

            val fullImageUrl = ApiClient.IMAGE_BASE_URL + historyData.imagePath

            Glide.with(itemView.context)
                .load(fullImageUrl)
                .into(binding.imageViewHistory)
        }
    }
}
