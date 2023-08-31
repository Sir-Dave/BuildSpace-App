package com.sirdave.buildspace.presentation.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sirdave.buildspace.R
import com.sirdave.buildspace.domain.model.SubscriptionPlan
import com.sirdave.buildspace.domain.model.User
import com.sirdave.buildspace.presentation.payment.PaymentEvent
import com.sirdave.buildspace.presentation.payment.PaymentState
import com.sirdave.buildspace.presentation.composables.CircularText
import com.sirdave.buildspace.presentation.payment.PaymentDialog
import com.sirdave.buildspace.presentation.credit_card.CardDetailsState
import com.sirdave.buildspace.presentation.credit_card.CardEvent
import com.sirdave.buildspace.presentation.credit_card.CreditCardDialog
import com.sirdave.buildspace.presentation.credit_card.OTPDialog
import com.sirdave.buildspace.presentation.subscription.SubscriptionEvent
import com.sirdave.buildspace.presentation.subscription.SubscriptionState
import com.sirdave.buildspace.ui.theme.LightBackground

@Composable
fun IndividualPlans(
    state: SubscriptionState,
    user: User?,
    paymentState: PaymentState,
    cardState: CardDetailsState,
    onSubscriptionEvent: (SubscriptionEvent) -> Unit,
    onPaymentEvent: (PaymentEvent) -> Unit,
    onNavigateToDashboard: () -> Unit,
    onEvent: (CardEvent) -> Unit,
    onClickProfileIcon: () -> Unit
){
    val subscriptionPlans = state.subscriptionPlans
    var showDialog by remember { mutableStateOf(false) }
    var showOtpDialog by remember { mutableStateOf(false) }
    var showPaymentDialog by remember { mutableStateOf(false) }

    var selectedPlan by remember { mutableStateOf<SubscriptionPlan?>(null) }

    LaunchedEffect(Unit) {
        onSubscriptionEvent(SubscriptionEvent.RefreshPlans)
    }

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

        Text(
            text = stringResource(id = R.string.subscription_plans),
            fontSize = 24.sp,
            fontWeight = FontWeight(700),
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
        )
        if (subscriptionPlans.isNotEmpty()){
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
                    .height(IntrinsicSize.Min)
            ) {
                PlanCard(
                    plan = subscriptionPlans[0],
                    icons = 1,
                    modifier = Modifier.weight(1f),
                    onPlanSelected = { selected, dialog ->
                        selectedPlan = selected
                        showDialog = dialog
                    }
                )

                PlanCard(
                    plan = subscriptionPlans[1],
                    icons = 2,
                    modifier = Modifier.weight(1f),
                    onPlanSelected = { selected, dialog ->
                        selectedPlan = selected
                        showDialog = dialog
                    }
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ){
                PlanCard(
                    plan = subscriptionPlans[2],
                    icons = 3,
                    onPlanSelected = { selected, dialog ->
                        selectedPlan = selected
                        showDialog = dialog
                    }
                )
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
