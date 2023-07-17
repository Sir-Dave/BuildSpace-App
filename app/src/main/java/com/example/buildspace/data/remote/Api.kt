package com.example.buildspace.data.remote

import com.example.buildspace.data.remote.dto.request.RegisterRequest
import com.example.buildspace.data.remote.dto.request.SignInRequest
import com.example.buildspace.data.remote.dto.response.ApiResponse
import com.example.buildspace.data.remote.dto.response.LoginResponse
import com.example.buildspace.data.remote.dto.response.SubscriptionPlanDto
import com.example.buildspace.data.remote.dto.response.SubscriptionDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface Api {

    companion object{
        const val BASE_URL = "https://buildspace-c6e1f17dbb33.herokuapp.com"
    }

    @POST("api/v1/auth/register")
    suspend fun registerUser(
        @Body registerRequest: RegisterRequest
    ): Response<ApiResponse>

    @POST("api/v1/auth/login")
    suspend fun loginUser(
        @Body signInRequest: SignInRequest
    ): Response<LoginResponse>


    @GET("api/v1/subscriptions")
    suspend fun getAllSubscriptions(
        @Query("userId") userId: String
    ): Response<List<SubscriptionDto>>


    @GET("api/v1/subscriptions/current")
    suspend fun getCurrentSubscription(
        @Query("userId") userId: String
    ): Response<SubscriptionDto>

    @GET("api/v1/subscriptions/id")
    suspend fun getSubscriptionById(
        @Path("id") id: String
    ): Response<SubscriptionDto>

    @GET("api/v1/subscriptions/plans")
    suspend fun getAllSubscriptionPlans(): Response<SubscriptionPlanDto>

}