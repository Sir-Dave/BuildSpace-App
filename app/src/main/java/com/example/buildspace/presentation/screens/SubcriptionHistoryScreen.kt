package com.example.buildspace.presentation.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.buildspace.R
import com.example.buildspace.presentation.SubscriptionViewModel
import com.example.buildspace.presentation.composables.CircularText
import com.example.buildspace.presentation.composables.SubscriptionCard
import com.example.buildspace.ui.theme.BuildSpaceTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SubscriptionHistory(
    viewModel: SubscriptionViewModel = hiltViewModel()
){
    val state by viewModel.subscriptionState.collectAsState()
    val subscriptionHistory = state.subscriptionList
    val user = viewModel.user!!

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        val initials by remember {
            mutableStateOf(
                "${user.firstName.substring(0, 1)}${user.lastName.substring(0, 1)}"
            )
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.End
        ){
            CircularText(
                text = initials,
                borderColor = Color.Black,
                modifier = Modifier
            )
        }
        Text(
            text = "Subscription History",
            fontSize = 24.sp,
            fontWeight = FontWeight(700),
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ){
            Row {
                FilterChip(
                    selected = false,
                    onClick = { /*TODO*/ },
                    label = { Text("Daily") }
                )

                Spacer(modifier = Modifier.width(4.dp))

                FilterChip(
                    selected = false,
                    onClick = { /*TODO*/ },
                    label = { Text("Weekly") }
                )

                Spacer(modifier = Modifier.width(4.dp))

                FilterChip(
                    selected = false,
                    onClick = { /*TODO*/ },
                    label = { Text("Monthly") }
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Row {
                FilterChip(
                    selected = false,
                    onClick = { /*TODO*/ },
                    label = { Text("") },
                    shape = RoundedCornerShape(
                        topStart = 8.dp,
                        bottomStart = 8.dp
                    ),
                    leadingIcon = {
                        Icon(
                            painter = painterResource(id = R.drawable.priority_high_24),
                            contentDescription = null,
                            tint = Color.Black
                        )
                    },
                    modifier = Modifier.width(35.dp)
                )

                FilterChip(
                    selected = false,
                    onClick = { /*TODO*/ },
                    label = { Text("") },
                    shape = RoundedCornerShape(
                        topEnd = 8.dp,
                        bottomEnd = 8.dp
                    ),
                    leadingIcon = {
                        Icon(imageVector = Icons.Default.Check, contentDescription = null)
                    },
                    modifier = Modifier.width(35.dp)
                )
            }
        }

        if (subscriptionHistory.isNotEmpty()){
            LazyColumn{
                items(items = subscriptionHistory){ subscription ->
                    SubscriptionCard(subscription = subscription)
                }
            }
        }
        else {
            //TODO: UI for empty subscriptions
        }
    }
}


@Preview(showBackground = true)
@Composable
fun SubscriptionHistoryPreview(){
    BuildSpaceTheme{
        SubscriptionHistory()
    }
}
