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

data class SubscriptionHistoryDto(
    val id: String,
    val amount: Float,
    val reference: String,
    val date: String,
    val status: String,
    val userEmail: String,
    val subscriptionType: String,
    val currency: String
)
