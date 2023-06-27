package com.example.buildspace.navigation

sealed class Screen(val route: String){
    object SignInScreen: Screen("sign_in")
    object SignUpScreen: Screen("sign_up")
    object DashboardScreen: Screen("dashboard")
    object SubscriptionHistoryScreen: Screen("subscription_history")
    object SubscriptionPlanScreen: Screen("subscription_plans")
}
