package com.sirdave.buildspace.data.remote.dto.response

data class ApiResponse(
    val timestamp: String,
    val httpStatusCode: Int,
    val httpStatus: String,
    val reason: String,
    val message: String
)
