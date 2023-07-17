package com.example.buildspace.data.mapper

import com.example.buildspace.data.remote.dto.response.UserDto
import com.example.buildspace.domain.model.User

fun UserDto.toUser(): User {

    return User(
        id = id,
        firstName = firstName,
        lastName = lastName,
        email = email,
        phoneNumber = phoneNumber,
        dateJoined = dateJoined,
        role = role,
    )
}