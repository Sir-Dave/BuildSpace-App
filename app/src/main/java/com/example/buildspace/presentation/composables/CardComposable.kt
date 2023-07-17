package com.example.buildspace.presentation.composables

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.buildspace.R
import com.example.buildspace.domain.model.SubscriptionPlan
import com.example.buildspace.ui.theme.BuildSpaceTheme

@Composable
fun CardDetails(plan: SubscriptionPlan){
    var cardNumber by remember{ mutableStateOf("") }
    var expiryDate by remember{ mutableStateOf("") }
    var cvc by remember{ mutableStateOf("") }
    var cardName by remember{ mutableStateOf("") }

    Column{
        Text(
            text = "Make Payment",
            style = TextStyle(
                fontSize = 24.sp,
                lineHeight = 32.sp,
                fontWeight = FontWeight(700),
                color = Color(0xFF201B15)
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            textAlign = TextAlign.Center
        )
        Text(
            text = "You are about to make payment for ${plan.name} at #${plan.amount}",
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            textAlign = TextAlign.Center,
            style = TextStyle(
                fontSize = 16.sp,
                lineHeight = 24.sp,
                fontWeight = FontWeight(500),
                color = Color(0xFF000000),
                letterSpacing = 0.15.sp,
            )
        )
        OutlinedTextField(
            value = cardNumber,
            onValueChange = { cardNumber = it
            },
            label = {
                Text(text = stringResource(R.string.card_number))
            },

            placeholder = {
                Text(text = stringResource(R.string.card_number))
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 8.dp, end = 8.dp),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                }
            )
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 8.dp, end = 8.dp)
        ) {
            OutlinedTextField(
                value = expiryDate,
                onValueChange = { expiryDate = it
                },
                label = {
                    Text(text = stringResource(R.string.expiry_date))
                },

                placeholder = {
                    Text(text = stringResource(R.string.expiry_date))
                },
                modifier = Modifier.weight(1f),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                    }
                )
            )
            Spacer(modifier = Modifier.width(16.dp))

            OutlinedTextField(
                value = cvc,
                onValueChange = { cvc = it
                },
                label = {
                    Text(text = stringResource(R.string.cvc))
                },

                placeholder = {
                    Text(text = stringResource(R.string.cvc))
                },
                modifier = Modifier.weight(1f),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                    }
                )
            )
        }

        OutlinedTextField(
            value = cardName,
            onValueChange = { cardName = it
            },
            label = { Text(text = stringResource(R.string.card_name)) },

            placeholder = {
                Text(text = stringResource(R.string.card_name))
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 8.dp, end = 8.dp),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                }
            )
        )

        Button(
            onClick = {},
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Black,
                contentColor = Color.White
            ),
            shape = RoundedCornerShape(8.dp),

            ) {
            Text(text = stringResource(id = R.string.pay))
        }

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyDialog(
    plan: SubscriptionPlan
){
    Dialog(onDismissRequest = { /*TODO*/ }) {
        OutlinedCard(
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier.padding(8.dp),
//            colors = CardDefaults.cardColors(
//                contentColor = Color(0xFFBE9870)
//            )
        ) {
            CardDetails(plan = plan)
        }
        
    }
}

@Composable
@Preview(showBackground = true)
fun CardDetailsPreview(){
    BuildSpaceTheme {
        MyDialog(
            plan = SubscriptionPlan(
                name = "Monthly",
                amount = "7000",
                numberOfDays = 28
            )
        )
    }
}