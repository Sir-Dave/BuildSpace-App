package com.sirdave.buildspace.data.remote.dto.response

data class SubscriptionDto(
    val id: String,
    val startDate: String,
    val endDate: String,
    val type: String,
    val amount: Double,
    val expired: Boolean,
    val user: UserDto
)

data class SubscriptionPlanDto(
    val name: String,
    val amount: Double,
    val numberOfDays: Int,
    val type: String
)

data class SubscriptionHistoryDto(
    val id: String,
    val amount: Double,
    val reference: String,
    val date: String,
    val status: String,
    val userEmail: String,
    val subscriptionType: String,
    val currency: String
)
