package com.sirdave.buildspace.presentation.auth.sign_up

sealed class SignUpFormEvent{
    data class FirstNameChanged(val firstName: String): SignUpFormEvent()
    data class LastNameChanged(val lastName: String): SignUpFormEvent()
    data class EmailChanged(val email: String): SignUpFormEvent()
    data class PhoneChanged(val phone: String): SignUpFormEvent()
    data class PasswordChanged(val password: String): SignUpFormEvent()
    data class RepeatedPasswordChanged(val repeatedPassword: String): SignUpFormEvent()
    object Submit: SignUpFormEvent()
}
