package com.example.buildspace.presentation.subscription

sealed class SubscriptionEvent{
    object GetCurrentSubscription: SubscriptionEvent()
    object RefreshCurrentSubscription: SubscriptionEvent()
    object GetPlans: SubscriptionEvent()
    object RefreshPlans: SubscriptionEvent()
    object GetHistory: SubscriptionEvent()
    object RefreshHistory: SubscriptionEvent()
    object RefreshAll: SubscriptionEvent()
}
