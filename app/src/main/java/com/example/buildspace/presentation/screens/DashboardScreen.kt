package com.example.buildspace.presentation.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.buildspace.R
import com.example.buildspace.domain.model.Subscription
import com.example.buildspace.presentation.subscription.SubscriptionViewModel
import com.example.buildspace.presentation.composables.CircularText
import com.example.buildspace.presentation.navigation.Screen
import com.example.buildspace.ui.theme.BuildSpaceTheme
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Dashboard(
    navHostController: NavHostController,
    viewModel: SubscriptionViewModel = hiltViewModel()
){
    val state by viewModel.subscriptionState.collectAsState()
    val currentSubscription = state.currentSubscription
    val user = viewModel.user

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        user?.let {
            val name by remember { mutableStateOf(it.firstName) }
            val initials by remember {
                mutableStateOf(
                    "${it.firstName.substring(0, 1)}${it.lastName.substring(0, 1)}"
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                val welcome = stringResource(id = R.string.welcome_back)
                Text(text = "$welcome $name", modifier = Modifier.padding(8.dp))
                CircularText(
                    text = initials,
                    borderColor = Color.Black,
                    modifier = Modifier
                )
            }
        }

        if (currentSubscription == null){
            UserNotSubscribed(navHostController)
        }

        else {
            val pairOfNumDays = getNumDaysUsed(currentSubscription)
            val daysUsed = pairOfNumDays.first
            val daysAllowable = pairOfNumDays.second

            Column(
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    text = "$daysUsed/$daysAllowable",
                    style = MaterialTheme.typography.displayLarge,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = stringResource(id = R.string.current_subscription),
                fontSize = 20.sp,
                fontWeight = FontWeight(700),
                letterSpacing = 0.15.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
            )

            OutlinedCard(
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth(),
                shape = RoundedCornerShape(8.dp),
                border = BorderStroke(1.dp, Color.Black)
            ) {
                Column(
                    modifier = Modifier.padding(8.dp),
                    verticalArrangement = Arrangement.SpaceAround,
                    horizontalAlignment = Alignment.Start,

                    ) {
                    Text(
                        text = "#${currentSubscription.amount} - ${currentSubscription.type}",
                        fontSize = 10.sp,
                        letterSpacing = 1.5.sp
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "${currentSubscription.startDate} - ${currentSubscription.endDate}",
                        fontSize = 16.sp,
                        fontWeight = FontWeight(500),
                        letterSpacing = 0.15.sp
                    )
                }
            }

            Button(
                onClick = {
                    navHostController.navigate(Screen.SubscriptionHistoryScreen.route)
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Black,
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),

                ) {
                Text(text = stringResource(R.string.btn_subscription_history).uppercase())
            }
        }
    }
}

@Composable
fun UserNotSubscribed(
    navHostController: NavHostController
){
    Column {
        Image(
            painter = painterResource(id = R.drawable.wireframe),
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .height(300.dp)
        )

        Text(
            text = stringResource(id = R.string.inactive_subscription),
            style = TextStyle(
                fontSize = 16.sp,
                lineHeight = 24.sp,
                fontWeight = FontWeight(500),
                color = Color(0xFF000000),
                textAlign = TextAlign.Center,
                letterSpacing = 0.15.sp
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        )

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = {
                navHostController.navigate(Screen.SubscriptionPlanScreen.route)
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Black,
                contentColor = Color.White
            ),
            shape = RoundedCornerShape(8.dp),

            ) {
            Text(text = stringResource(id = R.string.subscribe_now).uppercase())
        }
    }


}

private fun getNumDaysUsed(subscription: Subscription): Pair<String, String>{
    val numDays = when (subscription.type.lowercase()){
        "daily" -> 1
        "weekly" -> 7
        else -> 28
    }
    val duration = differenceInDaysFromToday(subscription.startDate)
    val daysUsed = formatNumberWithLeadingZeros(duration)
    val daysAllowable = formatNumberWithLeadingZeros(numDays)
    return Pair(daysUsed, daysAllowable)
}

private fun differenceInDaysFromToday(formattedDate: String): Int {
    val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
    val parsedDate = LocalDate.parse(formattedDate, formatter)
    val today = LocalDate.now()

    return ChronoUnit.DAYS.between(parsedDate, today).toInt()
}

private fun formatNumberWithLeadingZeros(number: Int, digits: Int = 2): String {
    return number.toString().padStart(digits, '0')
}

@Preview(showBackground = true)
@Composable
fun DashboardPreview(){
    BuildSpaceTheme{
        //Dashboard()
    }
}
