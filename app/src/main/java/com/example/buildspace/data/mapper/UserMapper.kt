package com.example.buildspace.data.mapper

import com.example.buildspace.data.remote.dto.response.UserDto
import com.example.buildspace.domain.model.User
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

fun UserDto.toUser(): User {
    val pattern = "yyyy-MM-dd HH:mm:ss"
    val formatter = DateTimeFormatter.ofPattern(pattern, Locale.getDefault())
    val date = LocalDateTime.parse(dateJoined, formatter)

    return User(
        id = id,
        firstName = firstName,
        lastName = lastName,
        email = email,
        phoneNumber = phoneNumber,
        dateJoined = date,
        role = role,
    )
}