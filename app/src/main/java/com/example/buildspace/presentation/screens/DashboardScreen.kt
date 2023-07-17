package com.example.buildspace.presentation.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.buildspace.presentation.SubscriptionViewModel
import com.example.buildspace.presentation.composables.CircularText
import com.example.buildspace.ui.theme.BuildSpaceTheme
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Dashboard(
    viewModel: SubscriptionViewModel = hiltViewModel()
){
    val state by viewModel.subscriptionState.collectAsState()
    val currentSubscription = state.currentSubscription

    val user = viewModel.user!!

    if (currentSubscription != null) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            val name by remember { mutableStateOf(user.firstName) }
            val initials by remember {
                mutableStateOf(
                    "${user.firstName.substring(0, 1)}${user.lastName.substring(0, 1)}"
                )
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ){
                Text(text = "Welcome back $name", modifier = Modifier.padding(8.dp))
                CircularText(
                    text = initials,
                    borderColor = Color.Black,
                    modifier = Modifier
                )
            }

            Column(
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    text = "02/28",
                    style = MaterialTheme.typography.displayLarge,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = "Current Subscription",
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
                        letterSpacing = 0.15.sp)
                }
            }

            Button(
                onClick = {},
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Black,
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),

                ) {
                Text(text = "See Subscription History".uppercase())
            }
        }
    }
    else{
        //TODO: UI when user is not subscribed
    }

}


@Preview(showBackground = true)
@Composable
fun DashboardPreview(){
    BuildSpaceTheme{
        Dashboard()
    }
}
