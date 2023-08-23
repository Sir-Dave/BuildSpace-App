package com.sirdave.buildspace.domain.use_cases

import android.util.Patterns

class ValidateEmail {

    fun execute(email: String): ValidationResult{
        if (email.isBlank()){
            return ValidationResult(
                isSuccessful = false,
                errorMessage = "Email cannot be blank"
            )
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            return ValidationResult(
                isSuccessful = false,
                errorMessage = "Invalid email address"
            )
        }
        return ValidationResult(
            isSuccessful = true
        )
    }
}