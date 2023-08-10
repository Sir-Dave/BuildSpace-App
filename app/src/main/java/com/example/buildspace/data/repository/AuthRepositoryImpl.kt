package com.example.buildspace.data.repository

import android.content.Context
import com.example.buildspace.data.remote.Api
import com.example.buildspace.data.remote.dto.request.RegisterRequest
import com.example.buildspace.data.remote.dto.request.SignInRequest
import com.example.buildspace.data.remote.dto.response.ApiResponse
import com.example.buildspace.data.remote.dto.response.LoginResponse
import com.example.buildspace.data.remote.dto.response.UserDto
import com.example.buildspace.domain.repository.AuthRepository
import com.example.buildspace.util.Resource
import com.example.buildspace.util.apiRequestFlow
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val api: Api,
    private val context: Context
) : AuthRepository{

    override suspend fun registerUser(registerRequest: RegisterRequest): Flow<Resource<ApiResponse>> {
        return apiRequestFlow(context) {
            api.registerUser(registerRequest)
        }
    }

    override suspend fun signInUser(signInRequest: SignInRequest): Flow<Resource<LoginResponse>> {
        return apiRequestFlow(context) {
            api.loginUser(signInRequest)
        }
    }

    override suspend fun getUserProfile(userId: String): Flow<Resource<UserDto>> {
        return apiRequestFlow(context){
            api.getUserProfile(userId)
        }
    }

    override suspend fun updateUserProfile(
        userId: String, firstName: String,
        lastName: String, phoneNumber: String): Flow<Resource<UserDto>> {
        return apiRequestFlow(context){
            api.updateUserProfile(userId, firstName, lastName, phoneNumber)
        }
    }
}