package com.example.buildspace

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.buildspace.domain.model.BottomNavItem
import com.example.buildspace.presentation.navigation.Navigation
import com.example.buildspace.presentation.navigation.Screen
import com.example.buildspace.ui.theme.BuildSpaceTheme
import com.example.buildspace.ui.theme.LightBackground
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BuildSpaceTheme {
                val navController = rememberNavController()
                val backStackEntry = navController.currentBackStackEntryAsState()
                var showBottomBar by rememberSaveable{
                    mutableStateOf(false)
                }

                showBottomBar = when(backStackEntry.value?.destination?.route) {
                    Screen.SignInScreen.route -> false
                    Screen.SignUpScreen.route -> false
                    else -> true
                }
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
                                    navController.navigate(it.route)
                                }
                            )
                    }
                ) {
                    Navigation(navHostController = navController)
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