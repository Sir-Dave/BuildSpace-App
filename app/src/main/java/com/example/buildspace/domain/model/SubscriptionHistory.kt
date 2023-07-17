package com.example.buildspace.domain.model

data class SubscriptionHistory(
    val id: String,
    val amount: String,
    val reference: String,
    val date: String,
    val status: String,
    val userEmail: String,
    val subscriptionType: String,
    val currency: String,
    val isSuccess: Boolean
)
