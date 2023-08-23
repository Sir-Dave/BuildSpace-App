package com.sirdave.buildspace.domain.model

data class User(
    val id: String,
    val firstName: String,
    val lastName: String,
    val email: String,
    val phoneNumber: String,
    val dateJoined: String,
    val role: String
)