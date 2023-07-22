package com.example.buildspace.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.example.buildspace.presentation.auth.sign_in.SignIn
import com.example.buildspace.presentation.auth.sign_up.SignUp
import com.example.buildspace.presentation.screens.Dashboard
import com.example.buildspace.presentation.screens.SubscriptionHistory
import com.example.buildspace.presentation.screens.SubscriptionPlans

@Composable
fun Navigation(navHostController: NavHostController, isRememberUser: Boolean){

    NavHost(
        navController = navHostController,
        startDestination = if (isRememberUser) Screen.HomeScreen.route
        else Screen.AuthScreen.route){

        navigation(
            route =  Screen.AuthScreen.route,
            startDestination = Screen.SignInScreen.route,
        ){
            composable(Screen.SignInScreen.route){
                SignIn(navHostController)
            }

            composable(Screen.SignUpScreen.route){
                SignUp(navHostController)
            }
        }

        navigation(
            route = Screen.HomeScreen.route,
            startDestination = Screen.DashboardScreen.route
        ){
            composable(Screen.DashboardScreen.route){
                Dashboard(navHostController = navHostController)
            }

            composable(Screen.SubscriptionPlanScreen.route){
                SubscriptionPlans(navHostController = navHostController)
            }

            composable(Screen.SubscriptionHistoryScreen.route){
                SubscriptionHistory()
            }
        }
    }
}