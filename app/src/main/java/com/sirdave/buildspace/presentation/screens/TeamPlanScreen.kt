package com.sirdave.buildspace.presentation.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.sirdave.buildspace.R
import com.sirdave.buildspace.domain.model.SubscriptionPlan
import com.sirdave.buildspace.presentation.composables.PlanCard
import com.sirdave.buildspace.presentation.credit_card.CardDetailsState
import com.sirdave.buildspace.presentation.credit_card.CardEvent
import com.sirdave.buildspace.presentation.credit_card.CreditCardDialog
import com.sirdave.buildspace.presentation.credit_card.OTPDialog
import com.sirdave.buildspace.presentation.payment.PaymentDialog
import com.sirdave.buildspace.presentation.payment.PaymentEvent
import com.sirdave.buildspace.presentation.payment.PaymentState
import com.sirdave.buildspace.presentation.subscription.SubscriptionEvent
import com.sirdave.buildspace.presentation.subscription.SubscriptionState

@Composable
fun TeamPlans(
    state: SubscriptionState,
    paymentState: PaymentState,
    cardState: CardDetailsState,
    onSubscriptionEvent: (SubscriptionEvent) -> Unit,
    onPaymentEvent: (PaymentEvent) -> Unit,
    onNavigateToDashboard: () -> Unit,
    onEvent: (CardEvent) -> Unit,
){
    val subscriptionPlans = state.subscriptionPlans
    var showDialog by remember { mutableStateOf(false) }
    var showOtpDialog by remember { mutableStateOf(false) }
    var showPaymentDialog by remember { mutableStateOf(false) }

    var selectedPlan by remember { mutableStateOf<SubscriptionPlan?>(null) }

    LaunchedEffect(Unit) {
        onSubscriptionEvent(SubscriptionEvent.GetTeamPlans)
    }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {

        if (showDialog && selectedPlan != null){
            CreditCardDialog(
                plan = selectedPlan!!,
                cardState = cardState,
                onDismissRequest = { showDialog = false },
                onEvent = onEvent
            )
        }

        if (showOtpDialog){
            OTPDialog(
                cardState = cardState,
                onDismissRequest = {
                    showOtpDialog = false
                    onPaymentEvent(PaymentEvent.ResetPaymentMessage)
                },
                onEvent = onEvent
            )
        }

        if (showPaymentDialog){
            val isSuccess = paymentState.error == null
            PaymentDialog(
                isSuccess = isSuccess,
                text = if (isSuccess) "Payment successful" else  paymentState.error!!,
                onDismissRequest = {
                    showPaymentDialog = false
                    if (isSuccess){
                        onPaymentEvent(PaymentEvent.ResetPaymentMessage)
                        onSubscriptionEvent(SubscriptionEvent.RefreshAll)
                    }
                    else{
                        onPaymentEvent(PaymentEvent.ResetPaymentError)
                    }
                },
                onNavigateToDashboard = onNavigateToDashboard
            )
        }

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp),
            content = {
                itemsIndexed(subscriptionPlans){ index, subscription ->
                    PlanCard(
                        plan = subscription,
                        icons = index + 1,
                        onPlanSelected = { selected, dialog ->
                            selectedPlan = selected
                            showDialog = dialog
                        }
                    )
                }
            }
        )

        if (subscriptionPlans.isNotEmpty()){
            Button(
                onClick = {},
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Black,
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                Text(text = stringResource(id = R.string.special_offers).uppercase())
            }
        }

        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            if (paymentState.isPaymentLoading) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    CircularProgressIndicator()
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = "Processing payment, please wait...")
                }
            }
        }

        if (paymentState.message != null){
            if (paymentState.message == "send_otp")
                showOtpDialog = true

            else showPaymentDialog = true
        }

        if (paymentState.error != null) {
            showPaymentDialog = true
        }
    }
}
