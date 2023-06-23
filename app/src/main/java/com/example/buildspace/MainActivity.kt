package com.example.buildspace

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.buildspace.models.BottomNavItem
import com.example.buildspace.ui.theme.Background
import com.example.buildspace.ui.theme.BuildSpaceTheme
import com.example.buildspace.ui.theme.LightBackground

class MainActivity : ComponentActivity() {
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BuildSpaceTheme {
                val navController = rememberNavController()
                Scaffold(
                    bottomBar = {
                        BottomNavigationBar(
                            items = listOf(
                                BottomNavItem(
                                    name = "Home",
                                    route = "home",
                                    icon = painterResource(id = R.drawable.outline_home_24)
                                ),

                                BottomNavItem(
                                    name = "Subscriptions",
                                    route = "subscriptions",
                                    icon = painterResource(id = R.drawable.outline_subscriptions_24)
                                ),

                                BottomNavItem(
                                    name = "History",
                                    route = "history",
                                    icon = painterResource(id = R.drawable.outline_history_24)
                                )
                            ) ,
                            navController = navController,
                            onItemClick = {
                                navController.navigate(it.route)
                            }
                        )
                    }
                ) {
                    Navigation(navHostController = navController)
                }
//                Surface(
//                    modifier = Modifier.fillMaxSize(),
//                ) {
//                    SignUp()
//                }
            }
        }
    }
}

@Composable
fun Navigation(navHostController: NavHostController){
    NavHost(navController = navHostController, startDestination = "home"){
        composable("home"){
            TempScreen(name = "Home")

        }
        composable("subscriptions"){
            TempScreen(name = "Subscriptions")

        }
        composable("history"){
            TempScreen(name = "History")
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

@Composable
fun TempScreen(name: String){
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ){
        Text(text = "$name screen")
    }
}