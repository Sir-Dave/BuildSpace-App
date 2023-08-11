package com.example.buildspace.presentation.user

sealed class UserEvent{
    data class Success(val message: String): UserEvent()
    data class Failure(val errorMessage: String): UserEvent()
}