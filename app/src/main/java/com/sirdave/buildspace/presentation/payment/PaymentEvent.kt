package com.sirdave.buildspace.presentation.payment

sealed class PaymentEvent{
    object ResetPaymentMessage: PaymentEvent()
    object ResetPaymentError: PaymentEvent()
}
