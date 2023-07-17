package com.example.buildspace.data.remote.dto.response

data class SubscriptionDto(
    val id: String,
    val startDate: String,
    val endDate: String,
    val type: String,
    val expired: Boolean,
    val user: UserDto
)

data class SubscriptionPlanDto(
    val name: String,
    val amount: Float,
    val numberOfDays: Int
)
