package com.sirdave.buildspace.domain.use_cases

class ValidateField {

    fun execute(text: String): ValidationResult{
        if (text.isBlank()){
            return ValidationResult(
                isSuccessful = false,
                errorMessage = "Field cannot be blank"
            )
        }

        return ValidationResult(
            isSuccessful = true
        )
    }
}