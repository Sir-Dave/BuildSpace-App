package com.example.buildspace.domain.repository

import com.example.buildspace.data.remote.dto.request.RegisterRequest
import com.example.buildspace.data.remote.dto.request.SignInRequest
import com.example.buildspace.data.remote.dto.response.ApiResponse
import com.example.buildspace.data.remote.dto.response.LoginResponse
import com.example.buildspace.data.remote.dto.response.UserDto
import com.example.buildspace.util.Resource
import kotlinx.coroutines.flow.Flow

interface AuthRepository {

    suspend fun registerUser(registerRequest: RegisterRequest): Flow<Resource<ApiResponse>>

    suspend fun signInUser(signInRequest: SignInRequest): Flow<Resource<LoginResponse>>

    suspend fun getUserProfile(userId: String): Flow<Resource<UserDto>>

    suspend fun updateUserProfile(userId: String, firstName: String,
                                  lastName: String, phoneNumber: String
    ): Flow<Resource<UserDto>>
}