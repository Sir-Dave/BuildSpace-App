package com.example.buildspace.data.repository

import com.example.buildspace.data.remote.Api
import com.example.buildspace.data.remote.dto.request.RegisterRequest
import com.example.buildspace.data.remote.dto.request.SignInRequest
import com.example.buildspace.data.remote.dto.response.ApiResponse
import com.example.buildspace.data.remote.dto.response.LoginResponse
import com.example.buildspace.domain.repository.AuthRepository
import com.example.buildspace.util.Resource
import com.example.buildspace.util.apiRequestFlow
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val api: Api
) : AuthRepository{

    override suspend fun registerUser(registerRequest: RegisterRequest): Flow<Resource<ApiResponse>> {
        return apiRequestFlow {
            api.registerUser(registerRequest)
        }
    }

    override suspend fun signInUser(signInRequest: SignInRequest): Flow<Resource<LoginResponse>> {
        return apiRequestFlow {
            api.loginUser(signInRequest)
        }
    }
}