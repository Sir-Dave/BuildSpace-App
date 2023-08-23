package com.sirdave.buildspace.domain.use_cases

class ValidateRepeatedPassword {

    fun execute(password: String, repeatedPassword: String): ValidationResult{

        if (password != repeatedPassword){
            return ValidationResult(
                isSuccessful = false,
                errorMessage = "Passwords don't match"
            )
        }
        return ValidationResult(
            isSuccessful = true
        )
    }
}