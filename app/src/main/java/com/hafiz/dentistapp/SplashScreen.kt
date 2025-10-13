package com.hafiz.dentistapp

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity

/**
 * Activity yang menampilkan splash screen (layar pembuka)
 * dan melakukan transisi ke LoginActivity setelah jeda waktu tertentu.
 */
class SplashScreen : AppCompatActivity() {

    // Konstanta untuk durasi splash screen: 3000 milidetik (3 detik).
    private val SPLASH_TIMEOUT: Long = 3000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Mengatur layout untuk Splash Activity.
        // Pastikan file layoutnya adalah 'activity_splash_screen.xml'.
        setContentView(R.layout.activity_splash_screen)

        // Menggunakan Handler untuk menunda eksekusi kode.
        // Tanda kurung kurawal {} di Kotlin menggantikan 'new Runnable()' di Java.
        Handler(Looper.getMainLooper()).postDelayed({
            // Membuat Intent untuk pindah dari SplashScreen ke LoginActivity.
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)

            // Memanggil finish() agar user tidak bisa kembali ke splash screen.
            finish()
        }, SPLASH_TIMEOUT)
    }
}
