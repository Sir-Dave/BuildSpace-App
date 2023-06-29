package com.example.buildspace.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.buildspace.R
import com.example.buildspace.SubscriptionsList
import com.example.buildspace.composables.CircularText
import com.example.buildspace.composables.SubscriptionCard
import com.example.buildspace.ui.theme.BuildSpaceTheme

val subscriptions = SubscriptionsList.items

@OptIn(ExperimentalMaterial3Api::class)
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

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ){
            Row {
                FilterChip(
                    selected = false,
                    onClick = { /*TODO*/ },
                    label = { Text("Daily") }
                )

                Spacer(modifier = Modifier.width(4.dp))

                FilterChip(
                    selected = false,
                    onClick = { /*TODO*/ },
                    label = { Text("Weekly") }
                )

                Spacer(modifier = Modifier.width(4.dp))

                FilterChip(
                    selected = false,
                    onClick = { /*TODO*/ },
                    label = { Text("Monthly") }
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Row {
                FilterChip(
                    selected = false,
                    onClick = { /*TODO*/ },
                    label = { Text("") },
                    shape = RoundedCornerShape(
                        topStart = 8.dp,
                        bottomStart = 8.dp
                    ),
                    leadingIcon = {
                        Icon(
                            painter = painterResource(id = R.drawable.priority_high_24),
                            contentDescription = null,
                            tint = Color.Black
                        )
                    },
                    modifier = Modifier.width(35.dp)
                )

                FilterChip(
                    selected = false,
                    onClick = { /*TODO*/ },
                    label = { Text("") },
                    shape = RoundedCornerShape(
                        topEnd = 8.dp,
                        bottomEnd = 8.dp
                    ),
                    leadingIcon = {
                        Icon(imageVector = Icons.Default.Check, contentDescription = null)
                    },
                    modifier = Modifier.width(35.dp)
                )
            }
        }

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
