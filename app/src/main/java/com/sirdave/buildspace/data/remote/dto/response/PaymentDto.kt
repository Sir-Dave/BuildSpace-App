package com.sirdave.buildspace.data.remote.dto.response

data class PaymentDto(
    val status: Boolean? = null,
    val message: String? = null,
    val event: String? = null,
    val data: Data
)

data class Data(
    val amount: Double,
    val currency: String,
    val transactionDate: String,
    val status: String,
    val reference : String,
    val domain : String,
    val gatewayResponse : String,
    val paidAt: String,
    val createdAt: String,
    val fees : Double,
    val message: String,
    val channel: String,
)
