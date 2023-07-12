package com.example.buildspace.domain.use_cases

data class ValidationResult(
    val isSuccessful: Boolean,
    val errorMessage: String? = null
)
