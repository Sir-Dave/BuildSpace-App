package com.example.buildspace.presentation.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.buildspace.data.local.AuthManager
import com.example.buildspace.domain.model.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class TokenViewModel @Inject constructor(
    private val authManager: AuthManager
): ViewModel() {

    private val token = MutableStateFlow<String?>(null)
    private val user = MutableStateFlow<User?>(null)


    init {
        viewModelScope.launch(Dispatchers.IO) {
            authManager.getToken().collect {
                withContext(Dispatchers.Main) {
                    token.value = it
                }
            }

            authManager.getUser().collect {
                withContext(Dispatchers.Main) {
                    user.value = it
                }
            }
        }
    }

    fun deleteToken() {
        viewModelScope.launch(Dispatchers.IO) {
            authManager.deleteToken()
        }
    }

    fun deleteUser(){
        viewModelScope.launch(Dispatchers.IO) {
            authManager.deleteUser()
        }
    }
}