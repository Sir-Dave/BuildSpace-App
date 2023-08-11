package com.example.buildspace.presentation.user

sealed class UserEvent{
    data class Failure(val errorMessage: String): UserEvent()
}