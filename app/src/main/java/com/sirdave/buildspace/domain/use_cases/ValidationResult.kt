package com.sirdave.buildspace.domain.use_cases

data class ValidationResult(
    val isSuccessful: Boolean,
    val errorMessage: String? = null
)
