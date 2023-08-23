package com.sirdave.buildspace.data.mapper

import com.sirdave.buildspace.data.remote.dto.response.UserDto
import com.sirdave.buildspace.domain.model.User

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