package com.hafiz.dentistapp

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.PopupWindow
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.hafiz.dentistapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var currentUserId: Int = -1
    private var currentUsername: String? = null

    private val handler = Handler(Looper.getMainLooper())
    private lateinit var runnable: Runnable
    private val slideInterval = 5000L // 5 seconds

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        currentUsername = intent.getStringExtra("USERNAME")
        currentUserId = intent.getIntExtra("USER_ID", -1)

        if (currentUsername != null) {
            binding.userGreeting.text = "Hi, $currentUsername"
        } else {
            binding.userGreeting.text = "Hi, User"
        }

        // Setup Image Slider
        setupImageSlider()

        binding.scanArea.setOnClickListener {
            val intent = Intent(this, CameraActivity::class.java)
            intent.putExtra("USER_ID", currentUserId)
            intent.putExtra("USERNAME", currentUsername)
            startActivity(intent)
        }

        binding.topBar.setOnClickListener {
            showLogoutPopupWindow()
        }

        binding.buttonRiwayat.setOnClickListener {
            val intent = Intent(this, HistoryActivity::class.java)
            intent.putExtra("USER_ID", currentUserId)
            intent.putExtra("USERNAME", currentUsername)
            startActivity(intent)
        }

        binding.buttonFasyankes.setOnClickListener {
            val intent = Intent(this, FasyankesActivity::class.java)
            intent.putExtra("USER_ID", currentUserId)
            intent.putExtra("USERNAME", currentUsername)
            startActivity(intent)
        }

        binding.buttonAskTootie.setOnClickListener {
            val intent = Intent(this, ChatbotActivity::class.java)
            startActivity(intent)
        }
    }

    private fun setupImageSlider() {
        // TODO: Replace with your actual drawable resources
        val imageList = listOf(
            R.drawable.slider_image_01, // Example image 1
            R.drawable.slider_image_02,  // Example image 2
            R.drawable.slider_image_03, // Example image 3
        )

        val imageSliderAdapter = ImageSliderAdapter(imageList)
        binding.imageSlider.adapter = imageSliderAdapter

        runnable = Runnable {
            var currentItem = binding.imageSlider.currentItem
            currentItem++
            if (currentItem >= imageSliderAdapter.itemCount) {
                currentItem = 0
            }
            binding.imageSlider.setCurrentItem(currentItem, true)
            handler.postDelayed(runnable, slideInterval)
        }
    }

    private fun showLogoutPopupWindow() {
        val inflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val popupView = inflater.inflate(R.layout.dialog_logout_content, null)

        val popupWindow = PopupWindow(
            popupView,
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT,
            true
        )

        popupWindow.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.dialog_logout_background))
        popupWindow.elevation = 20f

        val logoutLayout = popupView.findViewById<View>(R.id.layout_logout)
        logoutLayout.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
            popupWindow.dismiss()
        }

        popupWindow.showAsDropDown(binding.topBar, 0, 0, Gravity.END)
    }

    override fun onResume() {
        super.onResume()
        handler.postDelayed(runnable, slideInterval)
    }

    override fun onPause() {
        super.onPause()
        handler.removeCallbacks(runnable)
    }
}
