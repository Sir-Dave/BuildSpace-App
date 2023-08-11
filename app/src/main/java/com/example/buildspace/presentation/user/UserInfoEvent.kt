package com.example.buildspace.presentation.user

sealed class UserInfoEvent{
    data class FirstNameChanged(val firstName: String): UserInfoEvent()
    data class LastNameChanged(val lastName: String): UserInfoEvent()
    data class PhoneNumberChanged(val phoneNumber: String): UserInfoEvent()
    object LoadProfile: UserInfoEvent()
    object RefreshProfile: UserInfoEvent()
    object Submit: UserInfoEvent()
}


