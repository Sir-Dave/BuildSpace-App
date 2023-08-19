package com.example.buildspace

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.buildspace.domain.model.BottomNavItem
import com.example.buildspace.presentation.main.MainViewModel
import com.example.buildspace.presentation.navigation.Navigation
import com.example.buildspace.presentation.navigation.Screen
import com.example.buildspace.presentation.user.UserProfileScreen
import com.example.buildspace.presentation.user.UserViewViewModel
import com.example.buildspace.ui.theme.BuildSpaceTheme
import com.example.buildspace.ui.theme.LightBackground
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val tobeHidden = setOf(
            Screen.SignInScreen.route,
            Screen.SignUpScreen.route,
            Screen.MainScreen.route,
            Screen.LoadingScreen.route
        )

        setContent {
            val viewModel = viewModel<MainViewModel>()

            val sheetState = rememberModalBottomSheetState()
            val scope = rememberCoroutineScope()
            var showBottomSheet by remember { mutableStateOf(false) }

            BuildSpaceTheme {
                val navController = rememberNavController()
                val backStackEntry = navController.currentBackStackEntryAsState()
                var showBottomBar by rememberSaveable{
                    mutableStateOf(false)
                }

                showBottomBar = !tobeHidden.contains(backStackEntry.value?.destination?.route)

                Scaffold(
                    bottomBar = {
                        if (showBottomBar)
                            BottomNavigationBar(
                                items = listOf(
                                    BottomNavItem(
                                        name = "Home",
                                        route = Screen.DashboardScreen.route,
                                        icon = painterResource(id = R.drawable.outline_home_24)
                                    ),

                                    BottomNavItem(
                                        name = "Subscriptions",
                                        route = Screen.SubscriptionPlanScreen.route,
                                        icon = painterResource(id = R.drawable.outline_subscriptions_24)
                                    ),

                                    BottomNavItem(
                                        name = "History",
                                        route = Screen.SubscriptionHistoryScreen.route,
                                        icon = painterResource(id = R.drawable.outline_history_24)
                                    )
                                ),
                                navController = navController,
                                onItemClick = {
                                    navController.navigate(it.route){
                                        popUpTo(navController.graph.findStartDestination().id) {
                                            saveState = true
                                        }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                }
                            )
                    }
                ) { innerPadding ->
                    Navigation(
                        mainViewModel = viewModel,
                        navHostController = navController,
                        modifier = Modifier.padding(innerPadding),
                        toggleBottomSheet = {
                            showBottomSheet = showBottomSheet.not()
                        }
                    )

                    if (showBottomSheet){
                        val userViewModel = viewModel<UserViewViewModel>()
                        ModalBottomSheet(
                            onDismissRequest = { showBottomSheet = false },
                            sheetState = sheetState
                        ) {
                            UserProfileScreen(
                                userInfo = userViewModel.userInfoState,
                                formState = userViewModel.userFormState,
                                userEvent = userViewModel.userEvent,
                                onEvent = userViewModel::onEvent,
                                onDismiss = {
                                    scope.launch { sheetState.hide() }.invokeOnCompletion {
                                        if (!sheetState.isVisible) {
                                            showBottomSheet = false
                                        }
                                    }
                                },
                                onLogOut = {
                                    val currentRoute = backStackEntry.value?.destination?.route!!
                                    navController.navigate(Screen.AuthScreen.route){
                                        popUpTo(currentRoute){
                                            inclusive = true
                                        }
                                    }
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun BottomNavigationBar(
    items: List<BottomNavItem>,
    navController: NavHostController,
    modifier: Modifier = Modifier,
    onItemClick: (BottomNavItem) -> Unit
){
    val backStackEntry = navController.currentBackStackEntryAsState()

    NavigationBar(
        modifier = modifier,
        tonalElevation = 4.dp,
        containerColor = LightBackground
    ) {
        items.forEach { item ->

            val selected = item.route == backStackEntry.value?.destination?.route
            NavigationBarItem(
                selected = selected,
                onClick = { onItemClick(item) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Color.Black,
                    unselectedIconColor = Color.Gray
                ),
                icon = {
                    Column(horizontalAlignment = CenterHorizontally) {
                        Icon(painter = item.icon, contentDescription = item.name)
                        if (selected){
                            Text(
                                text = item.name,
                                textAlign = TextAlign.Center,
                                fontSize = 10.sp
                            )
                        }
                    }
                }
            )
        }
    }

}