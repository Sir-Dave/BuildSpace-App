package com.example.buildspace.data.remote

import com.example.buildspace.data.remote.dto.request.RegisterRequest
import com.example.buildspace.data.remote.dto.request.SignInRequest
import com.example.buildspace.data.remote.dto.response.ApiResponse
import com.example.buildspace.data.remote.dto.response.LoginResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface Api {

    companion object{
        const val BASE_URL = "https://buildspace-c6e1f17dbb33.herokuapp.com"
    }

    @POST("api/v1/auth/register")
    suspend fun registerUser(
        @Body registerRequest: RegisterRequest
    ): ApiResponse

    @POST("api/v1/auth/login")
    suspend fun loginUser(
        @Body signInRequest: SignInRequest
    ): LoginResponse
}