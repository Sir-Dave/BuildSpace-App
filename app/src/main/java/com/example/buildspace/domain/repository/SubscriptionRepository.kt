package com.example.buildspace.domain.repository

import com.example.buildspace.data.remote.dto.response.PaymentDto
import com.example.buildspace.domain.model.Subscription
import com.example.buildspace.domain.model.SubscriptionHistory
import com.example.buildspace.domain.model.SubscriptionPlan
import com.example.buildspace.util.Resource
import kotlinx.coroutines.flow.Flow

interface SubscriptionRepository {
    suspend fun getUserTransactionHistory(email: String, fetchFromRemote: Boolean): Flow<Resource<List<SubscriptionHistory>>>

    suspend fun getUserCurrentSubscription(userId: String, fetchFromRemote: Boolean): Flow<Resource<Subscription>>

    suspend fun getSubscriptionById(id: String): Flow<Resource<Subscription>>

    suspend fun getAllSubscriptionPlans(fetchFromRemote: Boolean): Flow<Resource<List<SubscriptionPlan>>>

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