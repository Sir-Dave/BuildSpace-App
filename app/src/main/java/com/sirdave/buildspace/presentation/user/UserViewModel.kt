package com.sirdave.buildspace.presentation.user

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sirdave.buildspace.data.local.AuthManager
import com.sirdave.buildspace.domain.model.User
import com.sirdave.buildspace.domain.repository.AuthRepository
import com.sirdave.buildspace.domain.use_cases.ValidateField
import com.sirdave.buildspace.domain.use_cases.ValidatePhone
import com.sirdave.buildspace.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject


@HiltViewModel
class UserViewViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val validateField: ValidateField,
    private val validatePhone: ValidatePhone,
    private val authManager: AuthManager
): ViewModel() {

    var userInfoState by mutableStateOf(UserInfoState())
    var userFormState by mutableStateOf(UserFormState())

    var user by mutableStateOf<User?>(null)

    init {
        viewModelScope.launch {
            authManager.getUser().collect{
                withContext(Dispatchers.Main){ user = it }
            }
        }
    }

    private val userEventChannel = Channel<UserEvent>()
    val userEvent = userEventChannel.receiveAsFlow()

    fun onEvent(event: UserInfoEvent){
        when (event){
            is UserInfoEvent.FirstNameChanged -> {
                userFormState = userFormState.copy(firstName = event.firstName)
            }
            is UserInfoEvent.LastNameChanged -> {
                userFormState = userFormState.copy(lastName = event.lastName)
            }
            is UserInfoEvent.PhoneNumberChanged -> {
                userFormState = userFormState.copy(phoneNumber = event.phoneNumber)
            }
            is UserInfoEvent.LoadProfile -> {
                getUserProfile(user?.id ?: "")
            }
            is UserInfoEvent.RefreshProfile -> {
                getUserProfile(user?.id ?: "", fetchFromRemote = true)
            }
            is UserInfoEvent.Submit -> {
                validateProfileInfo()
            }
            is UserInfoEvent.LogoutUser -> logout()
        }
    }

    private fun validateProfileInfo(){
        val firstNameResult = validateField.execute(userFormState.firstName)
        val lastNameResult = validateField.execute(userFormState.lastName)
        val phoneResult = validatePhone.execute(userFormState.phoneNumber)

        val hasError = listOf(
            firstNameResult,
            lastNameResult,
            phoneResult
        ).any{ !it.isSuccessful}

        if (hasError){
            userFormState = userFormState.copy(
                firstNameError = firstNameResult.errorMessage,
                lastNameError = lastNameResult.errorMessage,
                phoneNumberError = phoneResult.errorMessage,
            )
            return
        }
        updateUserProfile(
            userId = user?.id ?: "",
            firstName = userFormState.firstName,
            lastName = userFormState.lastName,
            phoneNumber = userFormState.phoneNumber
        )
    }

    private fun getUserProfile(userId: String, fetchFromRemote: Boolean = false) {
        viewModelScope.launch {
            userInfoState = userInfoState.copy(isLoading = true)
            val userResult = authRepository.getUserProfile(userId, fetchFromRemote)
            userResult.collect {
                withContext(Dispatchers.Main) {
                    when (val result = it) {
                        is Resource.Success -> {
                            userInfoState = userInfoState.copy(
                                isLoading = false,
                                error = null,
                                user = result.data
                            )
                            updateUserFormState(result.data!!)
                        }

                        is Resource.Error -> {
                            userInfoState = userInfoState.copy(
                                isLoading = false,
                                error = result.message!!
                            )
                            userEventChannel.send(UserEvent.Failure(userInfoState.error!!))
                        }

                        else -> Unit
                    }
                }
            }
        }
    }

    private fun updateUserProfile(userId: String, firstName: String,
                                  lastName: String, phoneNumber: String) {
        viewModelScope.launch {
            userInfoState = userInfoState.copy(isLoading = true)
            val userResult = authRepository.updateUserProfile(userId, firstName, lastName, phoneNumber)
            userResult.collect {
                withContext(Dispatchers.Main) {
                    when (val result = it) {
                        is Resource.Success -> {
                            userInfoState = userInfoState.copy(
                                isLoading = false,
                                error = null,
                                user = result.data
                            )
                            updateUserFormState(result.data!!)
                            userEventChannel.send(UserEvent.Success("Successfully updated profile"))
                        }

                        is Resource.Error -> {
                            userInfoState = userInfoState.copy(
                                isLoading = false,
                                error = result.message!!
                            )

                            userEventChannel.send(UserEvent.Failure(userInfoState.error!!))
                        }

                        else -> Unit
                    }
                }
            }
        }
    }

    private fun logout(){
        viewModelScope.launch {
            authRepository.logoutUser().collect {
                withContext(Dispatchers.Main) {
                    when (val result = it) {
                        is Resource.Success -> {
                            //authManager.deleteUser()
                            authManager.deleteToken()
                            authManager.clearUserLoginState()

                            userEventChannel.send(UserEvent.IsLoggedOut)
                        }

                        is Resource.Error -> {
                            userEventChannel.send(UserEvent.Failure(result.message!!))
                        }
                        else -> Unit
                    }
                }
            }
        }
    }

    private fun updateUserFormState(user: User){
        userFormState = userFormState.copy(
            firstName = user.firstName,
            lastName = user.lastName,
            email = user.email,
            phoneNumber = user.phoneNumber,
        )
    }

}