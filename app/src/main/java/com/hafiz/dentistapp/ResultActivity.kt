package com.hafiz.dentistapp

import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.hafiz.dentistapp.databinding.ActivityResultBinding
import java.io.File

class ResultActivity : AppCompatActivity() {

    private lateinit var binding: ActivityResultBinding
    private var currentUserId: Int = -1
    private var currentUsername: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val imagePath = intent.getStringExtra(EXTRA_IMAGE_PATH)
        val diseaseType = intent.getStringExtra(EXTRA_DISEASE_TYPE)
        val boundingBoxes = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableArrayListExtra(EXTRA_BOUNDING_BOXES, BoundingBox::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableArrayListExtra(EXTRA_BOUNDING_BOXES)
        }
        val timestamp = intent.getStringExtra("TIMESTAMP")
        currentUserId = intent.getIntExtra("USER_ID", -1)
        currentUsername = intent.getStringExtra("USERNAME")

        if (imagePath != null) {
            val imageFile = File(imagePath)
            if (imageFile.exists()) {
                Glide.with(this)
                    .load(imageFile)
                    .into(binding.resultImagePlaceholder)
            }
        }

        binding.resultText.text = diseaseType ?: "Tidak ada hasil"

        if (boundingBoxes != null && boundingBoxes.isNotEmpty()) {
            val averageConfidence = boundingBoxes.map { it.cnf }.average()
            binding.accuracyText.text = "${String.format("%.2f", averageConfidence * 100)}%"
        } else {
            binding.accuracyText.text = "N/A"
        }
        binding.accuracyLabel.text = "Confidence Score"

        binding.summaryText.text = getSummaryForDisease(diseaseType, timestamp)

        binding.homepageButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
                putExtra("USER_ID", currentUserId)
                putExtra("USERNAME", currentUsername)
            }
            startActivity(intent)
            finish()
        }
    }

    companion object {
        const val EXTRA_IMAGE_PATH = "extra_image_path"
        const val EXTRA_DISEASE_TYPE = "extra_disease_type"
        const val EXTRA_BOUNDING_BOXES = "extra_bounding_boxes"

        fun getSummaryForDisease(disease: String?, timestamp: String?): String {
            val detectionDate = "Deteksi dilakukan pada: ${timestamp ?: "Tidak diketahui"}"
            return when (disease?.lowercase()) {
                "calculus" -> "$detectionDate\n\nPenjelasan:\nKalkulus/ karang gigi adalah lapisan keras dan lengket yang terbentuk pada gigi. Karang gigi berawal dari plak, yang terdiri dari sisa makanan dan bakteri yang menempel pada gigi. Jika tidak dibersihkan, plak akan mengeras menjadi karang gigi, yang sulit dihilangkan hanya dengan menyikat gigi. Karang gigi biasanya berwarna kuning atau cokelat, dan jika tidak dirawat, dapat menyebabkan sakit gigi, gusi bengkak, dan bau mulut. Mengonsumsi banyak makanan manis, tidak menyikat gigi secara teratur, atau merokok dapat mempercepat terbentuknya karang gigi.\n\nTindakan Pencegahan dan Perawatan:\n1. Sikat gigi 2x sehari dengan pasta gigi mengandung fluoride dan gunakan benang gigi untuk membersihkan sela-sela gigi serta penggunaan obat kumur mengandung antiseptik\n2. Menjaga pola makan. Kurangi makanan manis penyebab karang gigi, mencukupi kebutuhan air putih, dan hindari kebiasaan merokok.\n3. Kontrol rutin ke dokter gigi\n4. Jika kalkulus atau karang gigi sudah terbentuk, maka tidak bisa dibersihkan dengan hanya disikat. Perawatan biasa tidak akan mampu membersihkan kalkulus yang sudah terlanjur menempel kuat pada gigi. Maka tindakan yang dapat dilakukan adalah Scalling gigi oleh dokter gigi untuk mengikis karang atau kalkulus yang menempel di permukaan gigi."
                "caries" -> "$detectionDate\n\nPenjelasan:\nKaries adalah kerusakan pada gigi yang biasanya disebut juga gigi berlubang. Hal ini terjadi karena bakteri di mulut mengubah sisa makanan, terutama yang mengandung gula menjadi asam. Asam ini kemudian mengikis lapisan luar gigi, sehingga lama-kelamaan gigi menjadi berlubang. Jika tidak segera diobati, kerusakan bisa makin parah dan menyebabkan rasa sakit atau infeksi.\n\nTindakan Pencegahan dan Perawatan:\n1. Menyikat gigi 2x sehari dengan pasta gigi mengandung fluoride, membersihkan sela-sela gigi dengan benang gigi dan penggunaan obat kumur.\n2. Membatasi konsumsi gula dan makanan manis serta menjaga pola makan\n3. Kontrol rutin ke dokter gigi\n4. Pada karies gigi yang telah mencapai dentin atau bagian lebih dalam dan menimbulkan kavitas (lubang) , tindakan remineralisasi seperti penggunaan fluoride umumnya tidak cukup untuk memperbaiki struktur gigi sehingga diperlukan tindakan yang lebih invasif seperti Penambalan Gigi.\n5. Pada karies gigi yang telah mencapai pulpa (saraf gigi) dan menimbulkan nyeri hebat biasanya membutuhkan Perawatan Saluran Akar (PSA)."
                "gingivitis" -> "$detectionDate\n\nPenjelasan:\nPeradangan pada gusi yang disebabkan oleh penumpukan plak gigi, biasanya ditandai dengan gusi yang berwarna merah, bengkak dan mudah berdarah saat menyikat gigi, rasa sakit ketika mengunyah.\n\nTindakan Pencegahan dan Perawatan:\n1. Menyikat gigi 2x sehari dengan pasta gigi mengandung fluoride, membersihkan sela-sela gigi dengan benang gigi dan penggunaan obat kumur untuk membantu mengurangi plak diantara gigi\n2. Membatasi konsumsi gula dan makanan manis serta menghindari kebiasaan merokok yang dapat memperparah peradangan gusi\n3. Scalling atau pembersihan karang gigi yang menempel pada gigi oleh dokter gigi\n4. Pemberian antibiotik dan obat pereda nyeri\n5. Kontrol rutin ke dokter gigi"
                "mouth ulcer" -> "$detectionDate\n\nPenjelasan:\nLuka kecil yang terbentuk di gusi, bibir, lidah, pipi bagian dalam, atau langit-langit mulut. Luka biasanya berwarna merah, kuning, atau putih. Banyak hal yang dapat menyebabkannya, termasuk cedera ringan, tidak sengaja menggigit pipi atau lidah, reaksi alergi terhadap bakteri tertentu, perubahan hormon, kurang tidur  dan stres emosional.\n\nTindakan Pencegahan dan Perawatan:\n1. Mencukupi kebutuhan air putih harian\n2. Menjaga kebersihan mulut seperti rutin sikat gigi 2x sehari dan penggunaan obat kumur\n3. Mengonsumsi makanan sehat, buah-buahan dan sayuran segar\n4. Hindari makanan panas dan pedas hingga luka sembuh\n5. Penggunaan anestesi topikal jika sariawan menimbulkan nyeri hebat.\n6. Sariawan akan hilang sendirinya dalam waktu 10-14 hari. Jika sariawan berlangsung lebih dari 3 minggu segera jadwalkan janji temu dengan dokter untuk dilakukan pemeriksaan lebih lanjut."
                "tooth discoloration" -> "$detectionDate\n\nPenjelasan:\nPerubahan warna gigi terjadi ketika warna gigi berubah. Gigi tampak menguning atau kurang cerah, atau mungkin muncul bintik-bintik putih atau gelap berwaran cokelat, ungu, atau abu-abu. Penyebabnya antara lain merokok dan penggunaan tembakau, adanya trauma pada gigi, kebersihan mulut yang buruk, penuaan, makanan dan minuman berwarna gelap yang meninggalkan noda pada gigi, dan konsumsi obat-obatan tertentu.\n\nTindakan Pencegahan dan Perawatan:\n1. Rutin sikat gigi 2x sehari dan membersihkan sela-sela gigi dengan benang gigi.\n2. Minum banyak air putih dan berkumur setelah minum-minuman yang dapat menodai gigi\n3. Batasi minuman penyebab noda gigi seperti teh, kopi, kola, dll\n4. Berhenti merokok dan kebiasaan mengunyah tembakau\n5. Kontrol rutin ke dokter gigi"
                else -> "Tidak ada ringkasan yang tersedia untuk hasil ini."
            }
        }
    }
}
