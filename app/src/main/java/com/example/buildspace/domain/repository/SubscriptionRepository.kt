package com.example.buildspace.domain.repository

import com.example.buildspace.data.remote.dto.response.SubscriptionDto
import com.example.buildspace.data.remote.dto.response.SubscriptionHistory
import com.example.buildspace.data.remote.dto.response.SubscriptionPlanDto
import com.example.buildspace.util.Resource
import kotlinx.coroutines.flow.Flow

interface SubscriptionRepository {

    suspend fun getAllUserSubscriptions(userId: String): Flow<Resource<List<SubscriptionDto>>>

    suspend fun getUserTransactionHistory(email: String): Flow<Resource<List<SubscriptionHistory>>>

    suspend fun getUserCurrentSubscription(userId: String): Flow<Resource<SubscriptionDto>>

    suspend fun getSubscriptionById(id: String): Flow<Resource<SubscriptionDto>>

    suspend fun getAllSubscriptionPlans(): Flow<Resource<SubscriptionPlanDto>>
}