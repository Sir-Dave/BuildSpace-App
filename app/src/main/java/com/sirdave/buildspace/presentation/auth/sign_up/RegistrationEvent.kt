package com.sirdave.buildspace.presentation.auth.sign_up

sealed class RegistrationEvent{
        data class Success(val message: String): RegistrationEvent()
        data class Failure(val errorMessage: String): RegistrationEvent()
    }