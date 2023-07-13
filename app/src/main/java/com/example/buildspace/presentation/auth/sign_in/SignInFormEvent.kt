package com.example.buildspace.presentation.auth.sign_in

sealed class SignInFormEvent{
    data class EmailChanged(val email: String): SignInFormEvent()
    data class PasswordChanged(val password: String): SignInFormEvent()
    object Submit: SignInFormEvent()
}
