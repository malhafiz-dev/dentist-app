package com.hafiz.dentistapp

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class FasyankesActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Panggil fungsi untuk meluncurkan pencarian di Google Maps
        launchGoogleMapsSearch()
    }

    /**
     * Membuat dan meluncurkan intent untuk mencari "klinik atau rumah sakit gigi terdekat"
     * di aplikasi Google Maps.
     */
    private fun launchGoogleMapsSearch() {
        // Kueri pencarian yang akan digunakan
        val searchQuery = "klinik atau rumah sakit gigi terdekat"
        
        // Buat URI untuk intent dengan skema geo. "0,0" adalah lokasi placeholder,
        // dan kueri 'q' akan membuat Google Maps mencari di sekitar lokasi pengguna saat ini.
        val gmmIntentUri = Uri.parse("geo:0,0?q=$searchQuery")
        
        // Buat Intent dengan action ACTION_VIEW
        val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
        
        // Atur paket ke Google Maps untuk memastikan intent dibuka di aplikasi yang benar
        mapIntent.setPackage("com.google.android.apps.maps")

        // Lakukan pemeriksaan untuk memastikan ada aplikasi yang bisa menangani intent ini (Google Maps terinstal)
        if (mapIntent.resolveActivity(packageManager) != null) {
            // Jalankan activity
            startActivity(mapIntent)
        } else {
            // Jika Google Maps tidak ditemukan, berikan pesan kepada pengguna
            Toast.makeText(this, "Aplikasi Google Maps tidak ditemukan.", Toast.LENGTH_LONG).show()
            // Anda bisa juga mengarahkan pengguna ke Play Store
            try {
                startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.google.android.apps.maps")))
            } catch (e: android.content.ActivityNotFoundException) {
                startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.google.android.apps.maps")))
            }
        }
        
        // Tutup FasyankesActivity setelah meluncurkan Maps, agar saat pengguna menekan "kembali"
        // dari Google Maps, mereka akan kembali ke MainActivity, bukan ke halaman kosong.
        finish()
    }
}
