package com.example.buildspace.data.repository

import com.example.buildspace.data.remote.Api
import com.example.buildspace.data.remote.dto.request.RegisterRequest
import com.example.buildspace.data.remote.dto.request.SignInRequest
import com.example.buildspace.data.remote.dto.response.ApiResponse
import com.example.buildspace.data.remote.dto.response.LoginResponse
import com.example.buildspace.domain.repository.AuthRepository
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val api: Api
) : AuthRepository{

    override suspend fun registerUser(registerRequest: RegisterRequest): ApiResponse {
        TODO("Not yet implemented")
    }

    override suspend fun signInUser(signInRequest: SignInRequest): LoginResponse {
        TODO("Not yet implemented")
    }

}