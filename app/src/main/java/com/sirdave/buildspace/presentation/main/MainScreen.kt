package com.sirdave.buildspace.presentation.main

import android.util.Log
import androidx.compose.runtime.*

@Composable
fun MainScreen(
    viewModel: MainViewModel,
    onNavigateToOnBoardingScreen: () -> Unit,
    onNavigateToLoginScreen: () -> Unit,
    onNavigateToHomeScreen: () -> Unit,
) {

    val viewState by remember(viewModel) { viewModel.viewState }
        .collectAsState(initial = ViewState.Loading)

    Log.d("MainScreen", "viewState is $viewState")

    when (viewState) {
        ViewState.Loading -> {
            LoadingScreen()
        }

        ViewState.OnBoarding -> {
            LaunchedEffect(viewState) {
                onNavigateToOnBoardingScreen()
            }
        }

        ViewState.NotLoggedIn -> {
            LaunchedEffect(viewState) {
                onNavigateToLoginScreen()
            }
        }

        ViewState.LoggedIn -> {
            LaunchedEffect(viewState){
                onNavigateToHomeScreen()
            }
        }
    }
}