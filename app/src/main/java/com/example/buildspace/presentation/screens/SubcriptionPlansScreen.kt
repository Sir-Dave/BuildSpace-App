package com.example.buildspace.presentation.screens

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
import com.example.buildspace.R
import com.example.buildspace.domain.model.SubscriptionPlan
import com.example.buildspace.domain.model.User
import com.example.buildspace.presentation.payment.PaymentEvent
import com.example.buildspace.presentation.payment.PaymentState
import com.example.buildspace.presentation.composables.CircularText
import com.example.buildspace.presentation.composables.PaymentDialog
import com.example.buildspace.presentation.credit_card.CardDetailsState
import com.example.buildspace.presentation.credit_card.CardEvent
import com.example.buildspace.presentation.credit_card.CreditCardDialog
import com.example.buildspace.presentation.credit_card.OTPDialog
import com.example.buildspace.presentation.subscription.SubscriptionEvent
import com.example.buildspace.presentation.subscription.SubscriptionState
import com.example.buildspace.ui.theme.LightBackground

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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlanCard(
    plan: SubscriptionPlan,
    icons: Int,
    onPlanSelected: (SubscriptionPlan, Boolean) -> Unit,
    modifier: Modifier = Modifier,
){
    OutlinedCard(
        modifier = modifier.padding(
                start = 8.dp,
                top = 8.dp,
                bottom = 8.dp
            ),
        border = BorderStroke(1.dp, Color.Black),
        colors = CardDefaults.cardColors(
            containerColor = LightBackground
        )
    ) {
        Column(
            modifier = modifier.padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
            ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                for (i in 1..icons) {
                    Icon(
                        painter = painterResource(id = R.drawable.electric_bolt_24),
                        contentDescription = null
                    )
                }
            }

            Text(
                text = plan.name,
                fontSize = 10.sp,
                letterSpacing = 1.5.sp,
                textAlign = TextAlign.Center
            )

            Button(
                onClick = {
                    onPlanSelected(plan, true)
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Black,
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(8.dp),
                modifier = modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                val pay = stringResource(id = R.string.pay).uppercase()
                Text(text = "$pay #${plan.amount}")
            }
        }
    }
}
