package com.sirdave.buildspace.domain.use_cases

class ValidatePassword {

    fun execute(password: String): ValidationResult{

        if (password.length < 8 ){
            return ValidationResult(
                isSuccessful = false,
                errorMessage = "Password should consist of at least 8 characters"
            )
        }

        val containsLetterAndDigit = password.any { it.isDigit() } && password.any { it.isLetter() }

        if (!containsLetterAndDigit){
            return ValidationResult(
                isSuccessful = false,
                errorMessage = "Password should have at least one letter and one digit"
            )
        }
        return ValidationResult(
            isSuccessful = true
        )
    }
}