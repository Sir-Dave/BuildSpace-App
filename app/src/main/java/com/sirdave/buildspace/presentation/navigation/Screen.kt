package com.sirdave.buildspace.presentation.navigation

sealed class Screen(val route: String){
    object SignInScreen: Screen("sign_in")
    object SignUpScreen: Screen("sign_up")
    object DashboardScreen: Screen("dashboard")
    object SubscriptionHistoryScreen: Screen("subscription_history")
    object SubscriptionPlanScreen: Screen("subscription_plans")
    object AuthScreen: Screen("auth")
    object HomeScreen: Screen("home")
    object MainScreen: Screen("main")
    object LoadingScreen: Screen("loading")
}
