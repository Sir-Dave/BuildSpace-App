package com.example.buildspace.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.buildspace.SubscriptionsList
import com.example.buildspace.composables.CircularText
import com.example.buildspace.composables.SubscriptionCard
import com.example.buildspace.ui.theme.BuildSpaceTheme

val subscriptions = SubscriptionsList.items

@Composable
fun SubscriptionHistory(){
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
            text = "Subscription History",
            fontSize = 24.sp,
            fontWeight = FontWeight(700),
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        )

        LazyColumn{
            items(items = subscriptions){ subscription ->
                SubscriptionCard(subscription = subscription)
            }
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
