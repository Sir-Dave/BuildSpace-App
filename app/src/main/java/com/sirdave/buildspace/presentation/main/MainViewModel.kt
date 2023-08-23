package com.sirdave.buildspace.presentation.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sirdave.buildspace.data.local.AuthManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val authManager: AuthManager
): ViewModel() {

    private val isRememberUser = MutableStateFlow(false)
    private val loginToken = MutableStateFlow<String?>(null)
    private val isFirstTimeUser = MutableStateFlow(false)

    val viewState = combine(
        isFirstTimeUser,
        loginToken,
        isRememberUser
    ) { isFirstTime, token, rememberUser ->
        if (isFirstTime) {
            ViewState.OnBoarding
        } else if (token != null && rememberUser) {
            ViewState.LoggedIn
        } else {
            ViewState.NotLoggedIn
        }
    }

    init {
        viewModelScope.launch(Dispatchers.IO) {
            val isFirstTimeDeferred = async { authManager.isFirstLogin().first() }
            val loginTokenDeferred = async { authManager.getToken().first() }
            val rememberUserDeferred = async { authManager.getUserLoginState().first() }

            val isFirstTime = isFirstTimeDeferred.await()
            val token = loginTokenDeferred.await()
            val rememberUser = rememberUserDeferred.await()

            withContext(Dispatchers.Main) {
                isFirstTimeUser.value = isFirstTime
                loginToken.value = token
                isRememberUser.value = rememberUser
            }
        }
    }

}