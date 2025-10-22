package com.hafiz.dentistapp.network

import com.hafiz.dentistapp.model.BaseResponse
import com.hafiz.dentistapp.model.HistoryData
import com.hafiz.dentistapp.model.UserData
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface UserService {

    @FormUrlEncoded
    @POST("register.php")
    fun registerUser(
        @Field("username") username: String,
        @Field("email") email: String,
        @Field("password") pass: String
    ): Call<BaseResponse<Any>>

    @FormUrlEncoded
    @POST("login.php")
    fun loginUser(
        @Field("username") username: String,
        @Field("password") pass: String
    ): Call<BaseResponse<UserData>>

    @Multipart
    @POST("save_detection.php")
    fun saveDetection(
        @Part("user_id") userId: RequestBody,
        @Part("type") type: RequestBody,
        @Part image: MultipartBody.Part
    ): Call<BaseResponse<Any>>

    @GET("get_history.php")
    fun getHistory(
        @Query("user_id") userId: Int
    ): Call<BaseResponse<List<HistoryData>>>
}
