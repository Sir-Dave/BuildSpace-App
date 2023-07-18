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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.buildspace.R
import com.example.buildspace.domain.model.SubscriptionPlan
import com.example.buildspace.presentation.SubscriptionViewModel
import com.example.buildspace.presentation.composables.CircularText
import com.example.buildspace.ui.theme.BuildSpaceTheme
import com.example.buildspace.ui.theme.LightBackground

@Composable
fun SubscriptionPlans(
    viewModel: SubscriptionViewModel = hiltViewModel()
){
    val state by viewModel.subscriptionState.collectAsState()
    val subscriptionPlans = state.subscriptionPlans
    val user = viewModel.user

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
                    modifier = Modifier
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
                    modifier = Modifier.weight(1f)
                )

                PlanCard(
                    plan = subscriptionPlans[1],
                    icons = 2,
                    modifier = Modifier.weight(1f)
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ){
                PlanCard(
                    plan = subscriptionPlans[2],
                    icons = 3
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
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlanCard(
    plan: SubscriptionPlan,
    icons: Int,
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
                onClick = {},
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


@Preview(showBackground = true)
@Composable
fun SubscriptionPlansPreview(){
    BuildSpaceTheme{
        SubscriptionPlans()
    }
}
