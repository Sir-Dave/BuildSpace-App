package com.example.buildspace.presentation.subscription

sealed class SubscriptionEvent{
    object RefreshCurrentSubscription: SubscriptionEvent()
    object RefreshPlans: SubscriptionEvent()
    object RefreshHistory: SubscriptionEvent()
    object RefreshAll: SubscriptionEvent()
}