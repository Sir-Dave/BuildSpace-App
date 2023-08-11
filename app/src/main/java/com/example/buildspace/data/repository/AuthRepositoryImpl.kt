package com.example.buildspace.data.repository

import android.content.Context
import com.example.buildspace.data.local.AuthManager
import com.example.buildspace.data.mapper.toUser
import com.example.buildspace.data.remote.Api
import com.example.buildspace.data.remote.dto.request.RegisterRequest
import com.example.buildspace.data.remote.dto.request.SignInRequest
import com.example.buildspace.data.remote.dto.response.ApiResponse
import com.example.buildspace.data.remote.dto.response.LoginResponse
import com.example.buildspace.domain.model.User
import com.example.buildspace.domain.repository.AuthRepository
import com.example.buildspace.util.Resource
import com.example.buildspace.util.apiRequestFlow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val api: Api,
    private val context: Context,
    private val authManager: AuthManager
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

    override suspend fun getUserProfile(userId: String, fetchFromRemote: Boolean): Flow<Resource<User>> {
        if (!fetchFromRemote){
            return authManager.getUser().map {
                Resource.Success(data = it)
            }
        }
        val request = apiRequestFlow(context){ api.getUserProfile(userId) }
        return request.map { dtoResource ->
            when(dtoResource){
                is Resource.Success -> {
                    val updatedUser = dtoResource.data!!.toUser()
                    authManager.saveUser(updatedUser)
                    Resource.Success(data = updatedUser)
                }
                is Resource.Error -> Resource.Error(message = dtoResource.message!!, data = null)
                else -> Resource.Loading(isLoading = false)
            }
        }
    }

    override suspend fun updateUserProfile(userId: String, firstName: String,
                                           lastName: String, phoneNumber: String): Flow<Resource<User>> {
        val request =  apiRequestFlow(context){
            api.updateUserProfile(userId, firstName, lastName, phoneNumber)
        }
        return request.map { dtoResource ->
            when(dtoResource){
                is Resource.Success -> {
                    val updatedUser = dtoResource.data!!.toUser()
                    authManager.saveUser(updatedUser)
                    Resource.Success(data = updatedUser)
                }
                is Resource.Error -> Resource.Error(message = dtoResource.message!!, data = null)
                else -> Resource.Loading(isLoading = false)
            }
        }
    }
}