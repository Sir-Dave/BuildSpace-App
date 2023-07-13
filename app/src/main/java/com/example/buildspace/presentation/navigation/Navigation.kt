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
fun Navigation(navHostController: NavHostController){

    NavHost(navController = navHostController, startDestination = Screen.AuthScreen.route){
        navigation(
            startDestination = Screen.SignInScreen.route,
            route =  Screen.AuthScreen.route
        ){
            composable(Screen.SignInScreen.route){
                SignIn(navHostController)
            }

            composable(Screen.SignUpScreen.route){
                SignUp(navHostController)
            }
        }

        navigation(
            startDestination = Screen.DashboardScreen.route,
            route = Screen.HomeScreen.route
        ){
            composable(Screen.DashboardScreen.route){
                Dashboard()
            }

            composable(Screen.SubscriptionPlanScreen.route){
                SubscriptionPlans()
            }

            composable(Screen.SubscriptionHistoryScreen.route){
                SubscriptionHistory()
            }
        }
    }
}