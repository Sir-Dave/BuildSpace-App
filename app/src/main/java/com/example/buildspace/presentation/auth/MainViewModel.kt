package com.example.buildspace.presentation.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.buildspace.data.local.AuthManager
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

    private val isUserRememberMe = MutableStateFlow<Boolean?>(null)


    init {
        viewModelScope.launch(Dispatchers.IO) {
            authManager.getUserLoginState().collect {
                withContext(Dispatchers.Main) {
                    isUserRememberMe.value = it
                }
            }
        }
    }
}