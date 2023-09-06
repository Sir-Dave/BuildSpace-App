package com.sirdave.buildspace.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.sirdave.buildspace.presentation.auth.sign_in.SignIn
import com.sirdave.buildspace.presentation.auth.sign_in.SignInViewModel
import com.sirdave.buildspace.presentation.auth.sign_up.SignUp
import com.sirdave.buildspace.presentation.auth.sign_up.SignUpViewModel
import com.sirdave.buildspace.presentation.main.MainScreen
import com.sirdave.buildspace.presentation.main.MainViewModel
import com.sirdave.buildspace.presentation.screens.Dashboard
import com.sirdave.buildspace.presentation.screens.SubscriptionHistory
import com.sirdave.buildspace.presentation.screens.SubscriptionPlans
import com.sirdave.buildspace.presentation.subscription.SubscriptionViewModel

@Composable
fun Navigation(
    mainViewModel: MainViewModel,
    navHostController: NavHostController,
    toggleBottomSheet: () -> Unit,
    modifier: Modifier = Modifier
){

    NavHost(
        navController = navHostController,
        startDestination = Screen.MainScreen.route,
        modifier = modifier
    ){
        composable(Screen.MainScreen.route) {
            MainScreen(
                viewModel = mainViewModel,
                onNavigateToOnBoardingScreen = {
                    navHostController.navigate(Screen.SignUpScreen.route){
                        popUpTo(Screen.MainScreen.route){
                            inclusive = true
                        }
                    }
                },
                onNavigateToLoginScreen = {
                    navHostController.navigate(Screen.SignInScreen.route){
                        popUpTo(Screen.MainScreen.route){
                            inclusive = true
                        }
                    }
                },
                onNavigateToHomeScreen = {
                    navHostController.navigate(Screen.HomeScreen.route){
                        popUpTo(Screen.MainScreen.route){
                            inclusive = true
                        }
                    }
                }
            )
        }

        navigation(
            route =  Screen.AuthScreen.route,
            startDestination = Screen.SignInScreen.route,
        ){
            composable(Screen.SignInScreen.route){
                val viewModel = hiltViewModel<SignInViewModel>()
                SignIn(
                    state = viewModel.authState.collectAsState().value,
                    loginState = viewModel.signInFormState,
                    validationEvent = viewModel.validationEvent,
                    onEvent = viewModel::onEvent,
                    onNavigateToHomeScreen = {
                        navHostController.navigate(Screen.HomeScreen.route) {
                            popUpTo(Screen.AuthScreen.route) {
                                inclusive = true
                            }
                        }
                    },
                    onNavigateToSignUpScreen = {
                        navHostController.navigate(Screen.SignUpScreen.route)
                    }
                )
            }

            composable(Screen.SignUpScreen.route){
                val viewModel = hiltViewModel<SignUpViewModel>()
                SignUp(
                    state = viewModel.authState.collectAsState().value,
                    signUpState = viewModel.signUpFormState,
                    registrationEvent = viewModel.registrationEvent,
                    onEvent = viewModel::onEvent,
                    onNavigateToSignInScreen = {
                        navHostController.navigate(Screen.SignInScreen.route)
                    }
                )
            }
        }

        navigation(
            route = Screen.HomeScreen.route,
            startDestination = Screen.DashboardScreen.route
        ){
            composable(Screen.DashboardScreen.route){
                val viewModel = hiltViewModel<SubscriptionViewModel>()

                Dashboard(
                    state = viewModel.subscriptionState.collectAsState().value,
                    user = viewModel.user,
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
                    onSubscriptionEvent = viewModel::onSubscriptionEvent,
                    errorEvent = viewModel.errorEvent,
                    onClickProfileIcon = {
                        toggleBottomSheet()
                    }
                )
            }

            composable(Screen.SubscriptionPlanScreen.route){
                val viewModel = hiltViewModel<SubscriptionViewModel>()
                SubscriptionPlans(
                    state = viewModel.subscriptionState.collectAsState().value,
                    user = viewModel.user,
                    paymentState = viewModel.paymentState,
                    cardState = viewModel.cardDetailsState,
                    onSubscriptionEvent = viewModel::onSubscriptionEvent,
                    onNavigateToDashboard = {
                        navHostController.navigate(Screen.DashboardScreen.route) {
                            popUpTo(Screen.SubscriptionPlanScreen.route) {
                                inclusive = true
                            }
                        }
                    },
                    onPaymentEvent = viewModel::onPaymentEvent,
                    onEvent = viewModel::onEvent,
                    onClickProfileIcon = {
                        toggleBottomSheet()
                    },
                    onNavigateToLogin = {
                        navHostController.navigate(Screen.SignInScreen.route){
                            popUpTo(Screen.DashboardScreen.route) { inclusive = true }
                        }
                    },
                    errorEvent = viewModel.errorEvent,
                )
            }

            composable(Screen.SubscriptionHistoryScreen.route){
                val viewModel = hiltViewModel<SubscriptionViewModel>()
                SubscriptionHistory(
                    state = viewModel.subscriptionState.collectAsState().value,
                    user = viewModel.user,
                    onSubscriptionEvent = viewModel::onSubscriptionEvent,
                    onClickProfileIcon = {
                        toggleBottomSheet()
                    },
                    onNavigateToLogin = {
                        navHostController.navigate(Screen.SignInScreen.route){
                            popUpTo(Screen.DashboardScreen.route) { inclusive = true }
                        }
                    },
                    errorEvent = viewModel.errorEvent,
                )
            }
        }
    }
}