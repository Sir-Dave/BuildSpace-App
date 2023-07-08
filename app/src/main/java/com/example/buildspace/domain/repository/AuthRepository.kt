package com.example.buildspace.domain.repository

import com.example.buildspace.data.remote.dto.request.RegisterRequest
import com.example.buildspace.data.remote.dto.request.SignInRequest
import com.example.buildspace.data.remote.dto.response.ApiResponse
import com.example.buildspace.data.remote.dto.response.LoginResponse

interface AuthRepository {

    suspend fun registerUser(registerRequest: RegisterRequest): ApiResponse

    suspend fun signInUser(signInRequest: SignInRequest): LoginResponse
}