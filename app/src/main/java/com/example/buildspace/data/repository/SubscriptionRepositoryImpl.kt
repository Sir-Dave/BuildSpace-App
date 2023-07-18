package com.example.buildspace.data.repository

import com.example.buildspace.data.remote.Api
import com.example.buildspace.data.remote.dto.response.PaymentDto
import com.example.buildspace.data.remote.dto.response.SubscriptionDto
import com.example.buildspace.data.remote.dto.response.SubscriptionHistoryDto
import com.example.buildspace.data.remote.dto.response.SubscriptionPlanDto
import com.example.buildspace.domain.repository.SubscriptionRepository
import com.example.buildspace.util.Resource
import com.example.buildspace.util.apiRequestFlow
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SubscriptionRepositoryImpl @Inject constructor(
    private val api: Api) : SubscriptionRepository{

    override suspend fun getUserTransactionHistory(email: String): Flow<Resource<List<SubscriptionHistoryDto>>> {
        return apiRequestFlow {
            api.getTransactionHistory(email)
        }
    }

    override suspend fun getUserCurrentSubscription(userId: String): Flow<Resource<SubscriptionDto>> {
        return apiRequestFlow {
            api.getCurrentSubscription(userId)
        }
    }

    override suspend fun getSubscriptionById(id: String): Flow<Resource<SubscriptionDto>> {
        return apiRequestFlow {
            api.getSubscriptionById(id)
        }
    }

    override suspend fun getAllSubscriptionPlans(): Flow<Resource<List<SubscriptionPlanDto>>> {
        return apiRequestFlow {
            api.getAllSubscriptionPlans()
        }
    }

    override suspend fun createSubscription(
        email: String,
        amount: Double,
        cardCvv: String,
        cardNumber: String,
        cardExpiryMonth: String,
        cardExpiryYear: String,
        pin: String,
        type: String
    ): Flow<Resource<PaymentDto>> {
        return apiRequestFlow {
            api.makePayment(
                email = email,
                amount = amount,
                cardCvv = cardCvv,
                cardNumber = cardNumber,
                cardExpiryMonth = cardExpiryMonth,
                cardExpiryYear = cardExpiryYear,
                pin = pin,
                type = type
            )
        }
    }

    override suspend fun sendOTP(otp: String, reference: String): Flow<Resource<PaymentDto>> {
        return apiRequestFlow {
            api.sendOTP(otp, reference)
        }
    }

}