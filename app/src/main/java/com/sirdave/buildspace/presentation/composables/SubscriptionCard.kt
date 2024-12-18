package com.sirdave.buildspace.presentation.composables

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sirdave.buildspace.R
import com.sirdave.buildspace.domain.model.SubscriptionHistory
import com.sirdave.buildspace.ui.theme.Green
import com.sirdave.buildspace.ui.theme.Red

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SubscriptionCard(
    subscription: SubscriptionHistory,
    modifier: Modifier = Modifier){
    Row (
        modifier = modifier.fillMaxWidth(),
    ){
        OutlinedCard(
            modifier = modifier
                .padding(
                    start = 8.dp,
                    top = 8.dp,
                    bottom = 8.dp
                )
                .weight(1f)
                .height(60.dp),

            shape = RoundedCornerShape(
                topStart = 8.dp,
                bottomStart = 8.dp
            ),
            border = BorderStroke(1.dp, Color.Black)
        ) {
            Column(
                modifier = Modifier.padding(8.dp),
                verticalArrangement = Arrangement.SpaceAround,
                horizontalAlignment = Alignment.Start,

                ) {
                Text(
                    text = "#${subscription.amount} - ${subscription.subscriptionType}",
                    fontSize = 10.sp,
                    letterSpacing = 1.5.sp
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = subscription.date,
                    fontSize = 16.sp,
                    fontWeight = FontWeight(500),
                    letterSpacing = 0.15.sp)
            }
        }

        OutlinedCard(
            modifier = modifier
                .padding(
                    top = 8.dp,
                    bottom = 8.dp,
                    end = 8.dp,
                )
                .height(60.dp),

            shape = RoundedCornerShape(
                topEnd = 8.dp,
                bottomEnd = 8.dp
            ),
            border = BorderStroke(1.dp, Color.Black),
            colors = CardDefaults.cardColors(
                containerColor = if (subscription.isSuccess) Green else Red
            )

        ) {

            Column(
                modifier = modifier
                    .padding(8.dp)
                    .fillMaxHeight(),
                verticalArrangement = Arrangement.Center

                ) {
                if (subscription.isSuccess)
                    Icon(imageVector = Icons.Default.Check, contentDescription = null)

                else Icon(
                    painter = painterResource(id = R.drawable.priority_high_24),
                    contentDescription = null,
                    tint = Color.Black
                )

            }
        }
    }

}