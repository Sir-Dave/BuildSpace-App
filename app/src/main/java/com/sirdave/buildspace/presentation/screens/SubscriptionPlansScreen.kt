package com.sirdave.buildspace.presentation.screens

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sirdave.buildspace.R
import com.sirdave.buildspace.domain.model.User
import com.sirdave.buildspace.presentation.ErrorEvent
import com.sirdave.buildspace.presentation.composables.CircularText
import com.sirdave.buildspace.presentation.credit_card.CardDetailsState
import com.sirdave.buildspace.presentation.credit_card.CardEvent
import com.sirdave.buildspace.presentation.payment.PaymentEvent
import com.sirdave.buildspace.presentation.payment.PaymentState
import com.sirdave.buildspace.presentation.subscription.SubscriptionEvent
import com.sirdave.buildspace.presentation.subscription.SubscriptionState
import kotlinx.coroutines.flow.Flow

@Composable
fun SubscriptionPlans(
    state: SubscriptionState,
    user: User?,
    paymentState: PaymentState,
    cardState: CardDetailsState,
    onSubscriptionEvent: (SubscriptionEvent) -> Unit,
    onPaymentEvent: (PaymentEvent) -> Unit,
    onNavigateToDashboard: () -> Unit,
    onEvent: (CardEvent) -> Unit,
    onClickProfileIcon: () -> Unit,
    onNavigateToLogin: () -> Unit,
    errorEvent: Flow<ErrorEvent>,
){
    val context = LocalContext.current

    LaunchedEffect(Unit){

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

    var tabIndex by remember { mutableStateOf(0) }

    val tabs = listOf("Individual", "Teams")
    
    Column(modifier = Modifier.fillMaxWidth()) {

        user?.let {
            val initials = "${it.firstName.substring(0, 1)}${it.lastName.substring(0, 1)}"
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
            text = stringResource(id = R.string.subscription_plans),
            fontSize = 24.sp,
            fontWeight = FontWeight(700),
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
        )

        Column(modifier = Modifier.fillMaxWidth()) {
            TabRow(
                selectedTabIndex = tabIndex,
                modifier = Modifier.padding(horizontal = 16.dp)
                    .align(Alignment.CenterHorizontally)
            ) {
                tabs.forEachIndexed { index, title ->
                    Tab(text = { Text(title) },
                        selected = tabIndex == index,
                        onClick = { tabIndex = index }
                    )
                }
            }
            when (tabIndex) {
                0 -> IndividualPlans(
                    state = state,
                    paymentState = paymentState,
                    cardState = cardState,
                    onSubscriptionEvent = onSubscriptionEvent,
                    onPaymentEvent = onPaymentEvent,
                    onNavigateToDashboard = onNavigateToDashboard,
                    onEvent = onEvent
                )
                1 -> TeamPlans(
                    state = state,
                    paymentState = paymentState,
                    cardState = cardState,
                    onSubscriptionEvent = onSubscriptionEvent,
                    onPaymentEvent = onPaymentEvent,
                    onNavigateToDashboard = onNavigateToDashboard,
                    onEvent = onEvent
                )
            }
        }
    }
}
