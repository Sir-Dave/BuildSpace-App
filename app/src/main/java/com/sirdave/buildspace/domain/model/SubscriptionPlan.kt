package com.sirdave.buildspace.domain.model

data class SubscriptionPlan(
    val name: String,
    val amount: String,
    val numberOfDays: Int,
    val type: String
)
