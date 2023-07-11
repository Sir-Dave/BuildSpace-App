package com.example.buildspace.presentation.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.buildspace.R
import com.example.buildspace.presentation.composables.CircularText
import com.example.buildspace.ui.theme.BuildSpaceTheme
import com.example.buildspace.ui.theme.LightBackground

@Composable
fun SubscriptionPlans(){
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        val initials by remember{ mutableStateOf("DV") }
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
            text = "Subscription Plan",
            fontSize = 24.sp,
            fontWeight = FontWeight(700),
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .height(IntrinsicSize.Min)
        ) {
            PlanCard(
                amount = "1000",
                type = "DAILY",
                icons = 1,
                modifier = Modifier.weight(1f)
            )

            PlanCard(
                amount = "2500",
                type = "WEEKLY",
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
                amount = "7000",
                type = "MONTHLY",
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
            Text(text = "See Special Offers")
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlanCard(
    amount: String,
    type: String,
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
                text = type,
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
                Text(text = "PAY #$amount")
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
