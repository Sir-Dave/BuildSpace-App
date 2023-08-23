package com.sirdave.buildspace.presentation.auth

import com.sirdave.buildspace.domain.model.User

data class AuthState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val statusCode: Int = 0,
    val token: String? = null,
    val user: User? = null
)