package com.sirdave.buildspace.presentation.auth.sign_in

sealed class SignInFormEvent{
    data class EmailChanged(val email: String): SignInFormEvent()
    data class PasswordChanged(val password: String): SignInFormEvent()
    data class RememberMeChanged(val isChecked: Boolean): SignInFormEvent()
    object Submit: SignInFormEvent()
}
