package com.sirdave.buildspace.presentation.composables

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sirdave.buildspace.R
import com.sirdave.buildspace.domain.model.SubscriptionPlan
import com.sirdave.buildspace.ui.theme.LightBackground

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
                Text(
                    text = "$pay #${plan.amount}",
                    fontSize = 12.sp
                )
            }
        }
    }
}