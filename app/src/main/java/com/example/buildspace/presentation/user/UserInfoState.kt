package com.example.buildspace.presentation.user

import com.example.buildspace.domain.model.User

data class UserState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val user: User? = null
)