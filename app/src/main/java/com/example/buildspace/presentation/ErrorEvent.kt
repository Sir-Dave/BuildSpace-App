package com.example.buildspace.presentation

sealed class ErrorEvent{
        object TokenExpiredEvent: ErrorEvent()
    }