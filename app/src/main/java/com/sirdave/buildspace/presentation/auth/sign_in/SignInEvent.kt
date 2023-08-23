package com.sirdave.buildspace.presentation.auth.sign_in

sealed class SignInEvent{
        object Success: SignInEvent()
        data class Failure(val errorMessage: String): SignInEvent()
    }