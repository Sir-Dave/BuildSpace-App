package com.example.buildspace.presentation.subscription

import com.example.buildspace.domain.model.Subscription
import com.example.buildspace.domain.model.SubscriptionHistory
import com.example.buildspace.domain.model.SubscriptionPlan

data class SubscriptionState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val currentSubscription: Subscription? = null,
    val subscriptionList: List<SubscriptionHistory> = emptyList(),
    val subscriptionPlans: List<SubscriptionPlan> = emptyList()
)
