package com.sirdave.buildspace.domain.repository

import com.sirdave.buildspace.data.remote.dto.request.RegisterRequest
import com.sirdave.buildspace.data.remote.dto.request.SignInRequest
import com.sirdave.buildspace.data.remote.dto.response.ApiResponse
import com.sirdave.buildspace.data.remote.dto.response.LoginResponse
import com.sirdave.buildspace.domain.model.User
import com.sirdave.buildspace.util.Resource
import kotlinx.coroutines.flow.Flow

interface AuthRepository {

    suspend fun registerUser(registerRequest: RegisterRequest): Flow<Resource<ApiResponse>>

    suspend fun signInUser(signInRequest: SignInRequest): Flow<Resource<LoginResponse>>

    suspend fun logoutUser(): Flow<Resource<ApiResponse>>

    suspend fun getUserProfile(userId: String, fetchFromRemote: Boolean): Flow<Resource<User>>

    suspend fun updateUserProfile(userId: String, firstName: String,
                                  lastName: String, phoneNumber: String): Flow<Resource<User>>
}