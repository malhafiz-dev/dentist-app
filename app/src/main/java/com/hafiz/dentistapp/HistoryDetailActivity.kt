package com.hafiz.dentistapp

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.hafiz.dentistapp.databinding.ActivityHistoryDetailBinding
import com.hafiz.dentistapp.network.ApiClient

class HistoryDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHistoryDetailBinding
    private var currentUserId: Int = -1
    private var currentUsername: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHistoryDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Menangkap semua data yang dikirim
        currentUserId = intent.getIntExtra("USER_ID", -1)
        currentUsername = intent.getStringExtra("USERNAME")
        val diseaseType = intent.getStringExtra("DISEASE_TYPE")
        val imagePath = intent.getStringExtra("IMAGE_PATH")
        val timestamp = intent.getStringExtra("TIMESTAMP")

        // Menampilkan data ke UI
        binding.resultText.text = diseaseType?.capitalize() ?: "Tidak ada data"
        binding.summaryText.text = ResultActivity.getSummaryForDisease(diseaseType, timestamp)


        // Memuat gambar
        val fullImageUrl = ApiClient.IMAGE_BASE_URL + imagePath
        Glide.with(this).load(fullImageUrl).into(binding.resultImage)
        binding.imageLabel.text = ""

        // Tombol kembali di atas
        binding.backButton.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        // Tombol Homepage di bawah
        binding.homepageButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java).apply {
                putExtra("USER_ID", currentUserId)
                putExtra("USERNAME", currentUsername)
                flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
            }
            startActivity(intent)
            finish()
        }
    }
}
