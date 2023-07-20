package com.example.buildspace.presentation.credit_card

data class PaymentState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val message: String? = null
)