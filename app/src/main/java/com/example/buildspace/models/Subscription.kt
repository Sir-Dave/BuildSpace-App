package com.example.buildspace.models

data class Subscription(
    val amount: Float,
    val type: String,
    val startDate: String,
    val endDate: String,
    val isSuccess: Boolean,
)
