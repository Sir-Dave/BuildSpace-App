package com.example.buildspace.domain.use_cases

class ValidatePhone {

    fun execute(text: String): ValidationResult{
        if (text.isNotBlank() && text.any { !it.isDigit() }){
            return ValidationResult(
                isSuccessful = false,
                errorMessage = "Must contain numbers only"
            )
        }

        return ValidationResult(
            isSuccessful = true
        )
    }
}