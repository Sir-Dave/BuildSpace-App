package com.sirdave.buildspace.presentation.auth.sign_in

data class SignInFormState(
    val email: String = "",
    val emailError: String? = null,
    val password: String = "",
    val passwordError: String? = null,
    val isRememberUser: Boolean = false
)
