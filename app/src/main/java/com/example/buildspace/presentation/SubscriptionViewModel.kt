package com.example.buildspace.presentation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.buildspace.data.local.AuthManager
import com.example.buildspace.data.mapper.toSubscription
import com.example.buildspace.data.mapper.toSubscriptionHistory
import com.example.buildspace.domain.model.User
import com.example.buildspace.domain.repository.SubscriptionRepository
import com.example.buildspace.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class SubscriptionViewModel @Inject constructor(
    private val repository: SubscriptionRepository,
    private val authManager: AuthManager
): ViewModel() {

    var user by mutableStateOf<User?>(null)
    private var _subscriptionState = MutableStateFlow(SubscriptionState())
    val subscriptionState: StateFlow<SubscriptionState> get() = _subscriptionState


    init {
        viewModelScope.launch(Dispatchers.IO) {
            authManager.getUser().collect {
                withContext(Dispatchers.Main) {
                    user = it
                }
            }
        }
    }


    private fun getCurrentSubscription(){
        viewModelScope.launch {
            _subscriptionState.value = _subscriptionState.value.copy(isLoading = true)
            user?.let {
                repository.getUserCurrentSubscription(it.id).collect{
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
            }
        }
    }

    private fun getSubscriptionHistory(){
        viewModelScope.launch {
            _subscriptionState.value = _subscriptionState.value.copy(isLoading = true)
            user?.let {
                repository.getUserTransactionHistory(it.email).collect{
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
        }
    }

    private fun getSubscriptionPlans(){
        viewModelScope.launch {
            repository.getAllSubscriptionPlans()
        }

    }

    private fun getSubscriptionById(id: String){
        viewModelScope.launch {
            repository.getSubscriptionById(id)
        }
    }
}