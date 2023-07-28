package com.example.buildspace.presentation

sealed class PaymentEvent{
    object ResetPaymentMessage: PaymentEvent()
    object ResetPaymentError: PaymentEvent()
}
