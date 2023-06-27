package com.example.buildspace.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.buildspace.screens.*

@Composable
fun Navigation(navHostController: NavHostController){

    NavHost(navController = navHostController, startDestination = Screen.SignInScreen.route){
        composable(Screen.SignInScreen.route){
            SignIn(navHostController)
        }

        composable(Screen.SignUpScreen.route){
            SignUp(navHostController)
        }

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