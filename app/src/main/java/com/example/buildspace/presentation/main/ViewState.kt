package com.example.buildspace.presentation.main

sealed class ViewState {
    object Loading: ViewState()
    object LoggedIn: ViewState()
    object NotLoggedIn: ViewState()
    object OnBoarding: ViewState()
}