package com.example.buildspace.presentation

sealed class ErrorEvent{
        object UserNeedsToLoginEvent: ErrorEvent()
    }