package com.example.buildspace.presentation.payment

sealed class PaymentEvent{
    object ResetPaymentMessage: PaymentEvent()
    object ResetPaymentError: PaymentEvent()
}
