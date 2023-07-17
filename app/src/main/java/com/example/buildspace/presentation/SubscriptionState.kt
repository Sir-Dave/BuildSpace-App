package com.example.buildspace.presentation

import com.example.buildspace.domain.model.Subscription
import com.example.buildspace.domain.model.SubscriptionHistory

data class SubscriptionState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val currentSubscription: Subscription? = null,
    val subscriptionList: List<SubscriptionHistory> = emptyList()
)
