package com.example.buildspace.presentation.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.buildspace.data.local.AuthManager
import com.example.buildspace.presentation.main.ViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val authManager: AuthManager
): ViewModel() {

    private val isRememberUser = MutableStateFlow(false)

    val viewState = isRememberUser.map { hasLoggedIn ->
        if (hasLoggedIn) {
            ViewState.LoggedIn
        } else {
            ViewState.NotLoggedIn
        }
    }

    init {
        viewModelScope.launch(Dispatchers.IO) {
            authManager.getUserLoginState().collect {
                isRememberUser.value = it
            }
        }
    }
}