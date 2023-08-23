package com.sirdave.buildspace.data.remote.dto.response

data class LoginResponse(
    val token: String,
    val user: UserDto
)