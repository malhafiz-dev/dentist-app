package com.hafiz.dentistapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.hafiz.dentistapp.databinding.ActivityRegisterBinding
import com.hafiz.dentistapp.model.BaseResponse
import com.hafiz.dentistapp.model.UserData
import com.hafiz.dentistapp.network.ApiClient
import com.hafiz.dentistapp.network.UserService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private val TAG = "RegisterActivity"
    // Konstanta untuk panjang password minimum
    private val MIN_PASSWORD_LENGTH = 6

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Set OnClickListener untuk Register Button
        binding.buttonRegister.setOnClickListener {
            registerUser()
        }

        // Set OnClickListener untuk Login TextView
        binding.textViewLogin.setOnClickListener {
            val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun registerUser() {
        // Menggunakan isNullOrBlank() yang lebih idiomatik untuk mengambil dan memeriksa input
        val username = binding.editTextUsername.text.toString().trim()
        val email = binding.editTextEmail.text.toString().trim()
        val password = binding.editTextPassword.text.toString().trim()
        val confirmPassword = binding.editTextConfirmPassword.text.toString().trim()

        // 1. Validasi semua kolom tidak kosong
        if (username.isNullOrBlank() || email.isNullOrBlank() || password.isNullOrBlank() || confirmPassword.isNullOrBlank()) {
            Toast.makeText(this, "Semua kolom wajib diisi", Toast.LENGTH_SHORT).show()
            return
        }

        // 2. Validasi format email
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, "Format email tidak valid", Toast.LENGTH_SHORT).show()
            return
        }

        // 3. Validasi panjang password
        if (password.length < MIN_PASSWORD_LENGTH) {
            Toast.makeText(this, "Password minimal harus $MIN_PASSWORD_LENGTH karakter", Toast.LENGTH_SHORT).show()
            return
        }

        // 4. Validasi kecocokan password
        if (password != confirmPassword) {
            Toast.makeText(this, "Konfirmasi password tidak cocok", Toast.LENGTH_SHORT).show()
            return
        }

        // API Call
        val userService = ApiClient.userService // Diubah di sini
        val call = userService.registerUser(username, email, password)

        call.enqueue(object : Callback<BaseResponse<Any>> { // Perubahan di sini
            override fun onResponse(
                call: Call<BaseResponse<Any>>, // Perubahan di sini
                response: Response<BaseResponse<Any>> // Perubahan di sini
            ) {
                if (response.isSuccessful) {
                    val apiResponse = response.body()
                    if (apiResponse != null && apiResponse.status == "success") {
                        Log.d(TAG, "Pendaftaran sukses untuk user: $username")
                        Toast.makeText(this@RegisterActivity, "Pendaftaran berhasil! Silakan masuk.", Toast.LENGTH_LONG).show()
                        // Navigate to LoginActivity
                        val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
                        startActivity(intent)
                        finish()
                    } else {
                        val message = apiResponse?.message ?: "Pendaftaran gagal. Respons server tidak valid."
                        Toast.makeText(this@RegisterActivity, message, Toast.LENGTH_LONG).show()
                    }
                } else {
                    Toast.makeText(this@RegisterActivity, "Pendaftaran gagal. Silakan coba lagi.", Toast.LENGTH_SHORT).show()
                    Log.e(TAG, "Response not successful. Code: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<BaseResponse<Any>>, t: Throwable) { // Perubahan di sini
                Toast.makeText(this@RegisterActivity, "Gagal terhubung ke server: ${t.message}", Toast.LENGTH_LONG).show()
                Log.e(TAG, "Failed to connect: ${t.message}")
            }
        })
    }
}
