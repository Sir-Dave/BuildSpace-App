package com.example.buildspace.presentation.credit_card

data class CardDetailsState(
    val cardNumber: String = "",
    val cardNumberError: String? = null,
    val cardExpiryDate: String = "",
    val cardExpiryDateError: String? = null,
    val cardCVV: String = "",
    val cardCVVError: String? = null
)