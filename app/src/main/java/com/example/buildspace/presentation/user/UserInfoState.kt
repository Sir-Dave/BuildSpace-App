package com.example.buildspace.presentation.user

import com.example.buildspace.domain.model.User

data class UserInfoState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val user: User? = null
)

data class UserFormState(
    val firstName: String = "",
    val firstNameError: String? = null,
    val lastName: String = "",
    val lastNameError: String? = null,
    val phoneNumber: String = "",
    val phoneNumberError: String? = null,
    val email: String = ""
)