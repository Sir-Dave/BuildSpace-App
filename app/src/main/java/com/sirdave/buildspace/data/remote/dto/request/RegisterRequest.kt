package com.sirdave.buildspace.data.remote.dto.request

data class RegisterRequest(
    var firstName: String,
    var lastName: String,
    var email: String,
    var phoneNumber: String?,
    var role: String,
    var password: String,
    var confirmPassword: String
)

