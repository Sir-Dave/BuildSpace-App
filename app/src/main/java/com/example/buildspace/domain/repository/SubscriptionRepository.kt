package com.example.buildspace.domain.repository

import com.example.buildspace.data.remote.dto.response.PaymentDto
import com.example.buildspace.data.remote.dto.response.SubscriptionDto
import com.example.buildspace.data.remote.dto.response.SubscriptionHistoryDto
import com.example.buildspace.data.remote.dto.response.SubscriptionPlanDto
import com.example.buildspace.util.Resource
import kotlinx.coroutines.flow.Flow

interface SubscriptionRepository {
    suspend fun getUserTransactionHistory(email: String): Flow<Resource<List<SubscriptionHistoryDto>>>

    suspend fun getUserCurrentSubscription(userId: String): Flow<Resource<SubscriptionDto>>

    suspend fun getSubscriptionById(id: String): Flow<Resource<SubscriptionDto>>

    suspend fun getAllSubscriptionPlans(): Flow<Resource<List<SubscriptionPlanDto>>>

    suspend fun createSubscription(
        email: String,
        amount: Double,
        cardCvv: String,
        cardNumber: String,
        cardExpiryMonth: String,
        cardExpiryYear: String,
        pin: String,
        type: String
    ): Flow<Resource<PaymentDto>>

    suspend fun sendOTP(otp: String, reference: String): Flow<Resource<PaymentDto>>
}