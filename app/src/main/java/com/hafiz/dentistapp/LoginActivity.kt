package com.hafiz.dentistapp

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.hafiz.dentistapp.databinding.ActivityLoginBinding
import com.hafiz.dentistapp.model.BaseResponse
import com.hafiz.dentistapp.model.UserData
import com.hafiz.dentistapp.network.ApiClient
import com.hafiz.dentistapp.network.UserService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private val TAG = "LoginActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.buttonLogin.setOnClickListener {
            loginUser()
        }

        binding.textViewRegister.setOnClickListener {
            val intent = Intent(this@LoginActivity, RegisterActivity::class.java)
            startActivity(intent)
        }
    }

    private fun loginUser() {
        val username = binding.editTextUsername.text.toString().trim()
        val password = binding.editTextPassword.text.toString().trim()

        if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Username dan password wajib diisi", Toast.LENGTH_SHORT).show()
            return
        }

        val call = ApiClient.userService.loginUser(username, password)

        call.enqueue(object : Callback<BaseResponse<UserData>> {
            override fun onResponse(
                call: Call<BaseResponse<UserData>>,
                response: Response<BaseResponse<UserData>>
            ) {
                if (response.isSuccessful) {
                    val apiResponse = response.body()
                    if (apiResponse != null && apiResponse.status == "success") {
                        Toast.makeText(this@LoginActivity, "Login berhasil!", Toast.LENGTH_SHORT).show()

                        val userData = apiResponse.data
                        val loggedInUsername = userData?.username
                        val userId = userData?.userId // DIUBAH: Menggunakan nama properti yang benar 'userId'

                        // Navigasi ke MainActivity dan kirim username dan user_id
                        val intent = Intent(this@LoginActivity, MainActivity::class.java)
                        intent.putExtra("USERNAME", loggedInUsername)
                        intent.putExtra("USER_ID", userId) // Tambahkan user_id ke Intent

                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        startActivity(intent)
                        finish()

                    } else {
                        val message = apiResponse?.message ?: "Login gagal. Respons server tidak valid."
                        Toast.makeText(this@LoginActivity, message, Toast.LENGTH_LONG).show()
                    }
                } else {
                    Toast.makeText(this@LoginActivity, "Login gagal. Cek kembali kredensial Anda.", Toast.LENGTH_SHORT).show()
                    Log.e(TAG, "Response not successful. Code: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<BaseResponse<UserData>>, t: Throwable) {
                Toast.makeText(this@LoginActivity, "Gagal terhubung ke server: ${t.message}", Toast.LENGTH_LONG).show()
                Log.e(TAG, "Failed to connect: ${t.message}")
            }
        })
    }
}
