package com.example.buildspace.presentation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.buildspace.data.local.AuthManager
import com.example.buildspace.data.mapper.toSubscription
import com.example.buildspace.data.mapper.toSubscriptionHistory
import com.example.buildspace.data.mapper.toSubscriptionPlan
import com.example.buildspace.domain.model.User
import com.example.buildspace.domain.repository.SubscriptionRepository
import com.example.buildspace.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import javax.inject.Inject

@HiltViewModel
class SubscriptionViewModel @Inject constructor(
    private val repository: SubscriptionRepository,
    private val authManager: AuthManager
): ViewModel() {

    var user by mutableStateOf<User?>(null)

    private val _subscriptionState = MutableStateFlow(SubscriptionState())
    val subscriptionState: StateFlow<SubscriptionState> = _subscriptionState

    init {
        viewModelScope.launch {

            coroutineScope {
                val userResult = async { authManager.getUser().first() }
                user = userResult.await()
            }

            _subscriptionState.value = _subscriptionState.value.copy(isLoading = true)
            user?.let {
                val currentSubscriptionDeferred = async { repository.getUserCurrentSubscription(it.id) }
                val subscriptionHistoryDeferred = async { repository.getUserTransactionHistory(it.email) }

                val currentSubscriptionResult = currentSubscriptionDeferred.await()
                val subscriptionHistoryResult = subscriptionHistoryDeferred.await()

                currentSubscriptionResult.collect{
                    withContext(Dispatchers.Main){
                        when (val result = it) {

                            is Resource.Success -> {
                                _subscriptionState.value = _subscriptionState.value.copy(
                                    isLoading = false,
                                    error = null,
                                    currentSubscription = result.data!!.toSubscription()
                                )
                            }

                            is Resource.Error -> {
                                _subscriptionState.value = _subscriptionState.value.copy(
                                    isLoading = false,
                                    error = result.message,
                                    currentSubscription = null
                                )
                            }

                            else -> Unit
                        }
                    }
                }

                subscriptionHistoryResult.collect{
                    withContext(Dispatchers.Main){
                        when (val result = it) {

                            is Resource.Success -> {
                                _subscriptionState.value = _subscriptionState.value.copy(
                                    isLoading = false,
                                    error = null,
                                    subscriptionList = result.data!!.map { history ->  history.toSubscriptionHistory() }
                                )
                            }

                            is Resource.Error -> {
                                _subscriptionState.value = _subscriptionState.value.copy(
                                    isLoading = false,
                                    error = result.message,
                                    subscriptionList = emptyList()
                                )
                            }
                            else -> Unit
                        }
                    }
                }
            }

            val subscriptionPlansDeferred = async { repository.getAllSubscriptionPlans() }
            val subscriptionPlansResult = subscriptionPlansDeferred.await()
            subscriptionPlansResult.collect{
                withContext(Dispatchers.Main){
                    when (val result = it) {

                        is Resource.Success -> {
                            _subscriptionState.value = _subscriptionState.value.copy(
                                isLoading = false,
                                error = null,
                                subscriptionPlans = result.data!!.map { it.toSubscriptionPlan() }
                            )
                        }

                        is Resource.Error -> {
                            _subscriptionState.value = _subscriptionState.value.copy(
                                isLoading = false,
                                error = result.message,
                                subscriptionPlans = emptyList()
                            )
                        }

                        else -> Unit
                    }
                }
            }
        }
    }

    private fun getSubscriptionById(id: String){
        viewModelScope.launch {
            repository.getSubscriptionById(id)
        }
    }
}