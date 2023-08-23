package com.sirdave.buildspace.presentation.credit_card

data class CardDetailsState(
    val cardNumber: String = "",
    val cardNumberError: String? = null,
    val cardExpiryDate: String = "",
    val cardExpiryDateError: String? = null,
    val cardCVV: String = "",
    val cardCVVError: String? = null,
    val cardPin: String = "",
    val cardPinError: String? = null,
    val cardOTP: String = "",
    val cardOTPError: String? = null
)