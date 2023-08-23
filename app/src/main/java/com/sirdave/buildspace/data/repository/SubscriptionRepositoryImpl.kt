package com.sirdave.buildspace.data.repository

import android.content.Context
import android.util.Log
import com.sirdave.buildspace.data.local.BuildSpaceDatabase
import com.sirdave.buildspace.data.mapper.*
import com.sirdave.buildspace.data.remote.Api
import com.sirdave.buildspace.data.remote.dto.response.PaymentDto
import com.sirdave.buildspace.domain.model.Subscription
import com.sirdave.buildspace.domain.model.SubscriptionHistory
import com.sirdave.buildspace.domain.model.SubscriptionPlan
import com.sirdave.buildspace.domain.repository.SubscriptionRepository
import com.sirdave.buildspace.util.Resource
import com.sirdave.buildspace.util.apiRequestFlow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class SubscriptionRepositoryImpl @Inject constructor(
    private val api: Api,
    private val context: Context,
    db: BuildSpaceDatabase) : SubscriptionRepository{

    private val dao = db.dao
    companion object{
        const val TAG = "SubscriptionRepository"
    }

    override suspend fun getUserTransactionHistory(email: String,
                                                   fetchFromRemote: Boolean
    ): Flow<Resource<List<SubscriptionHistory>>> {
        val localSubscriptionHistory = dao.getSubscriptionHistory()
        val shouldLoadFromCache = localSubscriptionHistory.isNotEmpty() && !fetchFromRemote

        if (shouldLoadFromCache) {
            Log.d(TAG, "fetching history from DB")
            return flow {
                emit(Resource.Success(data = localSubscriptionHistory.map { it.toSubscriptionHistory() } ))
            }
        }

        Log.d(TAG, "fetching history from remote server")
        val request = apiRequestFlow(context) { api.getTransactionHistory(email) }
        return request.map { dtoResource ->
            when (dtoResource){
                is Resource.Success -> {
                    val subscriptionHistoryDto = dtoResource.data!!
                    dao.clearHistory()
                    dao.insertSubscriptionHistory(
                        subscriptionHistoryDto.map { it.toSubscriptionHistoryEntity() }
                    )
                    Resource.Success(
                        data = subscriptionHistoryDto.map { it.toSubscriptionHistory() }
                    )
                }
                is Resource.Error -> Resource.Error(message = dtoResource.message!!, data = null)
                else -> Resource.Loading(isLoading = false)
            }
        }
    }

    override suspend fun getUserCurrentSubscription(userId: String,
                                                    fetchFromRemote: Boolean
    ): Flow<Resource<Subscription>> {
        val localCurrentSubscription = dao.getCurrentSubscription()
        val currentTimeMillis = System.currentTimeMillis()
        val cacheExpiryDurationMillis = 24 * 60 * 60 * 1000
        val isCacheValid = localCurrentSubscription?.let {
            (currentTimeMillis - it.timestamp) < cacheExpiryDurationMillis
        } ?: false

        val shouldLoadFromCache = isCacheValid && !fetchFromRemote

        if (shouldLoadFromCache) {
            Log.d(TAG, "fetching current subscription from DB")
            return flow {
                emit(Resource.Success(data = localCurrentSubscription?.toSubscription()))
            }
        }

        Log.d(TAG, "fetching current subscription from remote server")
        val request = apiRequestFlow(context) { api.getCurrentSubscription(userId) }
        return request.map { dtoResource ->
            when (dtoResource){
                is Resource.Success -> {
                    val subscriptionDto = dtoResource.data!!
                    dao.clearSubscription()
                    dao.insertSubscription(
                        subscriptionDto.toSubscriptionEntity(System.currentTimeMillis())
                    )
                    Resource.Success(data = subscriptionDto.toSubscription())
                }
                is Resource.Error -> Resource.Error(message = dtoResource.message!!, data = null)
                else -> Resource.Loading(isLoading = false)
            }
        }
    }

    override suspend fun getSubscriptionById(id: String): Flow<Resource<Subscription>> {
        val request = apiRequestFlow(context) { api.getSubscriptionById(id) }
        return request.map { dtoResource ->
            when (dtoResource){
                is Resource.Success -> {
                    val subscription = dtoResource.data!!.toSubscription()
                    Resource.Success(data = subscription)
                }
                is Resource.Error -> Resource.Error(message = dtoResource.message!!, data = null)
                else -> Resource.Loading(isLoading = false)
            }
        }
    }

    override suspend fun getAllSubscriptionPlans(fetchFromRemote: Boolean
    ): Flow<Resource<List<SubscriptionPlan>>> {
        val localSubscriptionPlans = dao.getSubscriptionPlans()
        val shouldLoadFromCache = localSubscriptionPlans.isNotEmpty() && !fetchFromRemote

        if (shouldLoadFromCache) {
            Log.d(TAG, "fetching plans from DB")
            return flow {
                emit(Resource.Success(data = localSubscriptionPlans.map { it.toSubscriptionPlan() } ))
            }
        }

        Log.d(TAG, "fetching plans from remote server")
        val request = apiRequestFlow(context) { api.getAllSubscriptionPlans() }
        return request.map { dtoResource ->
            when (dtoResource){
                is Resource.Success -> {
                    val plansDto = dtoResource.data!!
                    dao.clearPlans()
                    dao.insertSubscriptionPlans(plansDto.map { it.toSubscriptionPlanEntity() })
                    Resource.Success(
                        data = plansDto.map { it.toSubscriptionPlan() }
                    )
                }
                is Resource.Error -> Resource.Error(message = dtoResource.message!!, data = null)
                else -> Resource.Loading(isLoading = false)
            }
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
        return apiRequestFlow(context) {
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
        return apiRequestFlow(context) {
            api.sendOTP(otp, reference)
        }
    }

}