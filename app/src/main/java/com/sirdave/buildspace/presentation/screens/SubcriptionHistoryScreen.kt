package com.sirdave.buildspace.presentation.screens

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.sirdave.buildspace.R
import com.sirdave.buildspace.domain.model.User
import com.sirdave.buildspace.presentation.ErrorEvent
import com.sirdave.buildspace.presentation.composables.CircularText
import com.sirdave.buildspace.presentation.composables.SubscriptionCard
import com.sirdave.buildspace.presentation.subscription.SubscriptionEvent
import com.sirdave.buildspace.presentation.subscription.SubscriptionState
import kotlinx.coroutines.flow.Flow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SubscriptionHistory(
    state: SubscriptionState,
    user: User?,
    onSubscriptionEvent: (SubscriptionEvent) -> Unit,
    onClickProfileIcon: () -> Unit,
    onNavigateToLogin: () -> Unit,
    errorEvent: Flow<ErrorEvent>,
){
    val subscriptionHistory = state.subscriptionList
    val swipeRefreshState = rememberSwipeRefreshState(isRefreshing = state.isLoading)

    val context = LocalContext.current

    LaunchedEffect(Unit){
        onSubscriptionEvent(SubscriptionEvent.GetHistory)

        errorEvent.collect{ event ->
            when (event){
                ErrorEvent.UserNeedsToLoginEvent -> {
                    Toast.makeText(
                        context,
                        "Session expired, you need to login again",
                        Toast.LENGTH_SHORT
                    ).show()
                    onNavigateToLogin()
                }
            }
        }
    }

    val plans by remember {
        mutableStateOf(listOf("Daily", "Weekly", "Monthly",
            "Monthly (Team)", "Quarterly (Team)", "Biannual (Team)", "Yearly (Team)")
        )
    }

    var selectedPlan by remember { mutableStateOf("") }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        user?.let {
            val initials by remember {
                mutableStateOf(
                    "${it.firstName.substring(0, 1)}${it.lastName.substring(0, 1)}"
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
                    onClick = onClickProfileIcon
                )
            }
        }

        Text(
            text = stringResource(id = R.string.subscription_history),
            fontSize = 24.sp,
            fontWeight = FontWeight(700),
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        )

        LazyRow(modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)){
            items(plans){ plan ->
                FilterChip(
                    modifier = Modifier.padding(horizontal = 6.dp),
                    selected = selectedPlan == plan,
                    onClick = {
                        selectedPlan = if (selectedPlan == plan) "" else plan
                        onSubscriptionEvent(SubscriptionEvent.FilterHistory(selectedPlan))
                    },
                    label = { Text(plan) },
                )
            }
        }

        if (subscriptionHistory.isEmpty()){
            EmptySubscriptionHistory()
        }
        else {
            SwipeRefresh(
                state = swipeRefreshState,
                onRefresh = { onSubscriptionEvent(SubscriptionEvent.RefreshHistory) }) {
                LazyColumn {
                    items(items = subscriptionHistory) { subscription ->
                        SubscriptionCard(subscription = subscription)
                    }
                }
            }
        }
    }
}

@Composable
fun EmptySubscriptionHistory(){
    Column {
        Image(
            painter = painterResource(id = R.drawable.whiteboard),
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .height(300.dp)
        )

        Text(
            text = stringResource(id = R.string.no_subscription_history),
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
    }
}
