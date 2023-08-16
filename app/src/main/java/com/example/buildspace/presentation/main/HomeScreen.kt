package com.example.buildspace.presentation.main

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.buildspace.presentation.auth.MainViewModel
import com.example.buildspace.presentation.navigation.Screen
import com.example.buildspace.presentation.screens.Dashboard
import com.example.buildspace.presentation.subscription.SubscriptionViewModel

@Composable
fun HomeScreen(
    viewModel: MainViewModel,
    navHostController: NavHostController,
    onNavigateToLoginScreen: () -> Unit,
    toggleBottomSheet: () -> Unit,
) {
    
    val viewState by viewModel.viewState.collectAsState()

    Log.d("HomeScreen", "viewState is $viewState")

    when (viewState) {
        ViewState.Loading -> {
            LoadingScreen()
        }
        ViewState.NotLoggedIn -> {
            LaunchedEffect(viewState) {
                onNavigateToLoginScreen()
            }
        }
        ViewState.LoggedIn -> {
            val subscriptionViewModel = hiltViewModel<SubscriptionViewModel>()
            Dashboard(
                state = subscriptionViewModel.subscriptionState.collectAsState().value,
                user = subscriptionViewModel.user,
                onNavigateToHistory = {
                    navHostController.navigate(Screen.SubscriptionHistoryScreen.route)
                },

                onNavigateToPlans = {
                    navHostController.navigate(Screen.SubscriptionPlanScreen.route)
                },

                onNavigateToLogin = {
                    navHostController.navigate(Screen.SignInScreen.route){
                        popUpTo(Screen.DashboardScreen.route) { inclusive = true }
                    }
                },
                onSubscriptionEvent = subscriptionViewModel::onSubscriptionEvent,
                errorEvent = subscriptionViewModel.errorEvent,
                onClickProfileIcon = {
                    toggleBottomSheet()
                }
            )
        }
    }
}