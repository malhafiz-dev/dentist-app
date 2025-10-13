package com.hafiz.dentistapp

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.hafiz.dentistapp.adapter.HistoryAdapter
import com.hafiz.dentistapp.databinding.ActivityHistoryBinding
import com.hafiz.dentistapp.model.BaseResponse
import com.hafiz.dentistapp.model.HistoryData
import com.hafiz.dentistapp.network.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HistoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHistoryBinding
    private lateinit var historyAdapter: HistoryAdapter
    private var currentUserId: Int = -1
    private var currentUsername: String? = null // Variabel untuk menyimpan username

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        currentUserId = intent.getIntExtra("USER_ID", -1)
        currentUsername = intent.getStringExtra("USERNAME") // Menangkap username

        setupRecyclerView()
        fetchHistoryData()

        binding.backButton.setOnClickListener {
            finish() // Kembali ke MainActivity
        }
    }

    private fun setupRecyclerView() {
        historyAdapter = HistoryAdapter(emptyList()) { historyItem ->
            // Meneruskan USER_ID dan USERNAME ke HistoryDetailActivity
            val intent = Intent(this, HistoryDetailActivity::class.java).apply {
                putExtra("USER_ID", currentUserId)
                putExtra("USERNAME", currentUsername)
                putExtra("DISEASE_TYPE", historyItem.condition)
                putExtra("IMAGE_PATH", historyItem.imagePath)
                putExtra("TIMESTAMP", historyItem.timestamp)
            }
            startActivity(intent)
        }
        binding.recyclerViewHistory.apply {
            layoutManager = LinearLayoutManager(this@HistoryActivity)
            adapter = historyAdapter
        }
    }

    private fun fetchHistoryData() {
        if (currentUserId == -1) {
            Toast.makeText(this, "User ID tidak valid", Toast.LENGTH_SHORT).show()
            return
        }

        binding.progressBar.visibility = View.VISIBLE

        val call = ApiClient.userService.getHistory(currentUserId)
        call.enqueue(object : Callback<BaseResponse<List<HistoryData>>> {
            override fun onResponse(
                call: Call<BaseResponse<List<HistoryData>>>,
                response: Response<BaseResponse<List<HistoryData>>>
            ) {
                binding.progressBar.visibility = View.GONE
                if (response.isSuccessful) {
                    val historyList = response.body()?.data
                    if (!historyList.isNullOrEmpty()) {
                        historyAdapter.updateData(historyList)
                    } else {
                        Toast.makeText(this@HistoryActivity, "Tidak ada riwayat deteksi", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this@HistoryActivity, "Gagal memuat riwayat", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<BaseResponse<List<HistoryData>>>, t: Throwable) {
                binding.progressBar.visibility = View.GONE
                Toast.makeText(this@HistoryActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
