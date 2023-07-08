package com.example.buildspace.domain.model

import java.time.LocalDateTime

data class User(
    val id: String,
    val firstName: String,
    val lastName: String,
    val email: String,
    val phoneNumber: String,
    val dateJoined: LocalDateTime,
    val role: String
)