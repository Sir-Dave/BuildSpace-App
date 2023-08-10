package com.example.buildspace.presentation.user

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.buildspace.domain.repository.AuthRepository
import com.example.buildspace.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject


@HiltViewModel
class UserViewViewModel @Inject constructor(
    private val authRepository: AuthRepository
): ViewModel() {

    var userState by mutableStateOf(UserState())

    private fun getUserProfile(fetchFromRemote: Boolean = false) {
        viewModelScope.launch {
            userState = userState.copy(isLoading = true)
            val userResult = authRepository.getUserProfile(fetchFromRemote)
            userResult.collect {
                withContext(Dispatchers.Main) {
                    when (val result = it) {
                        is Resource.Success -> {
                            userState = userState.copy(
                                isLoading = false,
                                error = null,
                                user = result.data
                            )
                        }

                        is Resource.Error -> {
                            userState = userState.copy(
                                isLoading = false,
                                error = result.message!!
                            )
                        }

                        else -> Unit
                    }
                }
            }
        }
    }

    private fun updateUserProfile(firstName: String, lastName: String, phoneNumber: String) {
        viewModelScope.launch {
            userState = userState.copy(isLoading = true)
            val userResult =
                authRepository.updateUserProfile(firstName, lastName, phoneNumber)
            userResult.collect {
                withContext(Dispatchers.Main) {
                    when (val result = it) {
                        is Resource.Success -> {
                            userState = userState.copy(
                                isLoading = false,
                                error = null,
                                user = result.data
                            )
                        }

                        is Resource.Error -> {
                            userState = userState.copy(
                                isLoading = false,
                                error = result.message!!
                            )
                        }

                        else -> Unit
                    }
                }
            }
        }
    }
}