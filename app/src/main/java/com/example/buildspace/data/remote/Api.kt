package com.example.buildspace.data.remote

import com.example.buildspace.data.remote.dto.request.RegisterRequest
import com.example.buildspace.data.remote.dto.request.SignInRequest
import com.example.buildspace.data.remote.dto.response.*
import retrofit2.Response
import retrofit2.http.*

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

    @GET("api/v1/users/{id}")
    suspend fun getUserProfile(
        @Path("id") userId: String
    ): Response<UserDto>

    @FormUrlEncoded
    @PUT("api/v1/users/{id}")
    suspend fun updateUserProfile(
        @Path("id") userId: String,
        @Field("firstName") firstName: String?,
        @Field("lastName") lastName: String?,
        @Field("phoneNumber") phoneNumber: String?
    ): Response<UserDto>

    @GET("api/v1/transactions")
    suspend fun getTransactionHistory(
        @Query("email") email: String
    ): Response<List<SubscriptionHistoryDto>>

    @GET("api/v1/subscriptions/current")
    suspend fun getCurrentSubscription(
        @Query("userId") userId: String
    ): Response<SubscriptionDto>

    @GET("api/v1/subscriptions/id")
    suspend fun getSubscriptionById(
        @Path("id") id: String
    ): Response<SubscriptionDto>

    @GET("api/v1/subscriptions/plans")
    suspend fun getAllSubscriptionPlans(): Response<List<SubscriptionPlanDto>>

    @FormUrlEncoded
    @POST("api/v1/payments")
    suspend fun makePayment(
        @Field("email") email: String,
        @Field("amount") amount: Double,
        @Field("cardCvv") cardCvv: String,
        @Field("cardNumber") cardNumber: String,
        @Field("cardExpiryMonth") cardExpiryMonth: String,
        @Field("cardExpiryYear") cardExpiryYear: String,
        @Field("pin") pin: String,
        @Field("subscriptionType") type: String
    ): Response<PaymentDto>

    @FormUrlEncoded
    @POST("api/v1/payments/send-otp")
    suspend fun sendOTP(
        @Field("otp") otp: String,
        @Field("reference") reference: String
    ): Response<PaymentDto>

}