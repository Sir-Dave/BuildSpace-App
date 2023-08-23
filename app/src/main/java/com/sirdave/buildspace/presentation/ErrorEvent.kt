package com.sirdave.buildspace.presentation

sealed class ErrorEvent{
        object UserNeedsToLoginEvent: ErrorEvent()
    }