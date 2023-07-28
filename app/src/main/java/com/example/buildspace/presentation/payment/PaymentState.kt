package com.example.buildspace.presentation.payment

data class PaymentState(
    val isPaymentLoading: Boolean = false,
    val error: String? = null,
    val message: String? = null,
    val reference: String? = null
)
