package com.example.buildspace.presentation.credit_card

import com.example.buildspace.domain.model.SubscriptionPlan


sealed class CardEvent {
    data class CardNumberChanged(val number: String): CardEvent()
    data class CardExpiryDateChanged(val expiryDate: String): CardEvent()
    data class CardCVCChanged(val cvc: String): CardEvent()
    data class CardPinChanged(val pin: String): CardEvent()
    data class CardOTPChanged(val otp: String): CardEvent()
    data class Submit(val plan: SubscriptionPlan): CardEvent()
    object SendOTP: CardEvent()
}