package com.sirdave.buildspace.presentation.subscription

sealed class SubscriptionEvent{
    object GetCurrentSubscription: SubscriptionEvent()
    object RefreshCurrentSubscription: SubscriptionEvent()
    object GetPlans: SubscriptionEvent()
    object RefreshPlans: SubscriptionEvent()
    object GetHistory: SubscriptionEvent()
    object RefreshHistory: SubscriptionEvent()
    data class FilterHistory(val planType: String): SubscriptionEvent()
    object RefreshAll: SubscriptionEvent()
}
