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

    private val _isRememberUser = MutableStateFlow(false)
    val isRememberUser = _isRememberUser.asStateFlow()

    val viewState = _isRememberUser.map { hasLoggedIn ->
        if (hasLoggedIn) {
            ViewState.LoggedIn
        } else {
            ViewState.NotLoggedIn
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = ViewState.Loading
    )

    init {
        viewModelScope.launch(Dispatchers.IO) {
            authManager.getUserLoginState().collect {
                _isRememberUser.value = it
            }
        }
    }
}