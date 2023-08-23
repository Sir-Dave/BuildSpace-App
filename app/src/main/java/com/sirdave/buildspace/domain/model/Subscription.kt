package com.sirdave.buildspace.domain.model

data class Subscription(
    val id: String,
    val startDate: String,
    val endDate: String,
    val type: String,
    val amount: String,
    val expired: Boolean,
)
