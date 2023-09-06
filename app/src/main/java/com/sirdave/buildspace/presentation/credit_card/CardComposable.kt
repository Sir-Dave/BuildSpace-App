package com.sirdave.buildspace.presentation.credit_card

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.sirdave.buildspace.R
import com.sirdave.buildspace.domain.model.SubscriptionPlan

@Composable
fun CreditCardDialog(
    plan: SubscriptionPlan,
    cardState: CardDetailsState,
    onDismissRequest: () -> Unit,
    onEvent: (CardEvent) -> Unit
){
    Dialog(onDismissRequest = { onDismissRequest() }) {
        OutlinedCard(
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier.padding(8.dp),
        ) {
            DebitCardComposable(
                plan = plan,
                cardState = cardState,
                onDismissRequest = onDismissRequest,
                onEvent = onEvent
            )
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun OTPDialog(
    cardState: CardDetailsState,
    onDismissRequest: () -> Unit,
    onEvent: (CardEvent) -> Unit
){
    val controller = LocalSoftwareKeyboardController.current

    Dialog(onDismissRequest = { onDismissRequest() }) {
        OutlinedCard(
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier.padding(8.dp),
        ) {

            Column{
                Text(
                    text = "Please enter the OTP sent to your phone number.",
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
                    value = cardState.cardOTP,
                    onValueChange = {
                        onEvent(CardEvent.CardOTPChanged(it))
                    },
                    label = {
                        if (cardState.cardOTPError != null) {
                            Text(
                                text = cardState.cardOTPError,
                                fontSize = 11.sp
                            )
                        } else {
                            Text(text = stringResource(R.string.otp))
                        }
                    },
                    placeholder = {
                        Text(text = stringResource(R.string.otp))
                    },
                    isError = cardState.cardOTPError != null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 8.dp, end = 8.dp),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            controller?.hide()
                        }
                    )
                )

                Button(
                    onClick = {
                        onEvent(CardEvent.SendOTP)
                        if (cardState.cardOTPError == null)
                            onDismissRequest()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Black,
                        contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(8.dp),

                    ) {
                    Text(text = stringResource(id = R.string.send))
                }
            }
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun DebitCardComposable(
    plan: SubscriptionPlan,
    cardState: CardDetailsState,
    onDismissRequest: () -> Unit,
    onEvent: (CardEvent) -> Unit
){
    val controller = LocalSoftwareKeyboardController.current

    val expirationDateFocusRequester = remember{ FocusRequester() }
    val cvvFocusRequester = remember{ FocusRequester() }
    val pinFocusRequester = remember{ FocusRequester() }

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
            text = "You are about to make payment for ${plan.name.lowercase()} at #${plan.amount}",
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
            value = cardState.cardNumber,
            onValueChange = { onEvent(CardEvent.CardNumberChanged(it.take(16)))
            },
            label = {
                if (cardState.cardNumberError != null){
                    Text(
                        text = cardState.cardNumberError,
                        fontSize = 11.sp
                    )
                }
                else {
                    Text(text = stringResource(R.string.card_number))
                }
            },

            placeholder = {
                Text(text = stringResource(R.string.card_number))
            },
            isError = cardState.cardNumberError != null,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 8.dp, end = 8.dp),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Next
            ),
            keyboardActions = KeyboardActions(
                onNext =  {
                    expirationDateFocusRequester.requestFocus()
                }
            ),
            visualTransformation = CardNumberTransformation()
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 8.dp, end = 8.dp)
        ) {
            OutlinedTextField(
                value = cardState.cardExpiryDate,
                onValueChange = {
                    onEvent(CardEvent.CardExpiryDateChanged(it.take(4)))

                    if (cardState.cardExpiryDate.length == 5) {
                        cvvFocusRequester.requestFocus()
                    }
                },
                label = {
                    if (cardState.cardExpiryDateError != null){
                        Text(
                            text = cardState.cardExpiryDateError,
                            fontSize = 11.sp
                        )
                    }
                    else {
                        Text(text = stringResource(R.string.expiry_date))
                    }
                },
                placeholder = {
                    Text(text = stringResource(R.string.expiry_date))
                },
                isError = cardState.cardExpiryDateError != null,
                modifier = Modifier
                    .weight(1f)
                    .focusRequester(expirationDateFocusRequester),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(
                    onNext =  {
                        cvvFocusRequester.requestFocus()
                    }
                ),
                visualTransformation = CardExpirationDateTransformation()
            )
            Spacer(modifier = Modifier.width(16.dp))

            OutlinedTextField(
                value = cardState.cardCVV,
                onValueChange = {
                    onEvent(CardEvent.CardCVCChanged(it.take(3)))

                    if (cardState.cardCVV.length == 3) {
                        pinFocusRequester.requestFocus()
                    }
                },
                label = {
                    if (cardState.cardCVVError != null){
                        Text(
                            text = cardState.cardCVVError,
                            fontSize = 11.sp
                        )
                    }
                    else {
                        Text(text = stringResource(R.string.cvc))
                    }
                },
                placeholder = {
                    Text(text = stringResource(R.string.cvc))
                },
                isError = cardState.cardCVVError != null,
                modifier = Modifier
                    .weight(1f)
                    .focusRequester(cvvFocusRequester),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(
                    onNext =  {
                        pinFocusRequester.requestFocus()
                    }
                )
            )
        }

        OutlinedTextField(
            value = cardState.cardPin,
            onValueChange = {
                onEvent(CardEvent.CardPinChanged(it.take(4)))
            },
            label = {
                if (cardState.cardPinError != null){
                    Text(
                        text = cardState.cardPinError,
                        fontSize = 11.sp
                    )
                }
                else {
                    Text(text = stringResource(R.string.card_pin))
                }
            },
            placeholder = {
                Text(text = stringResource(R.string.card_pin))
            },
            isError = cardState.cardPinError != null,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 8.dp, end = 8.dp)
                .focusRequester(pinFocusRequester)
            ,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = { controller?.hide() }
            )
        )

        Button(
            onClick = {
                onEvent(CardEvent.Submit(plan))
                if (!hasError(cardState))
                    onDismissRequest()
            },
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

fun hasError(cardState: CardDetailsState): Boolean{
    if (cardState.cardOTPError != null || cardState.cardCVVError ==  null ||
                cardState.cardNumberError != null || cardState.cardExpiryDateError != null)
        return true

    return false
}