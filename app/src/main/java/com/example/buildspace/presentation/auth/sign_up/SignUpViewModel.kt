package com.example.buildspace.presentation.auth.sign_up

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.buildspace.data.remote.dto.request.RegisterRequest
import com.example.buildspace.domain.repository.AuthRepository
import com.example.buildspace.domain.use_cases.*
import com.example.buildspace.presentation.auth.AuthState
import com.example.buildspace.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val validateField: ValidateField,
    private val validateEmail: ValidateEmail,
    private val validatePassword: ValidatePassword,
    private val validateRepeatedPassword: ValidateRepeatedPassword,
): ViewModel() {


    private val _authState = MutableStateFlow(AuthState())
    val authState: StateFlow<AuthState> get() = _authState


    var signUpFormState by mutableStateOf(SignUpFormState())

    private val registrationEventChannel = Channel<RegistrationEvent>()
    val registrationEvent = registrationEventChannel.receiveAsFlow()

    fun onEvent(event: SignUpFormEvent){
        when(event){
            is SignUpFormEvent.FirstNameChanged -> {
                signUpFormState = signUpFormState.copy( firstName = event.firstName)
            }
            is SignUpFormEvent.LastNameChanged -> {
                signUpFormState = signUpFormState.copy( lastName = event.lastName)
            }
            is SignUpFormEvent.EmailChanged -> {
                signUpFormState = signUpFormState.copy( email = event.email)
            }
            is SignUpFormEvent.PhoneChanged -> {
                signUpFormState = signUpFormState.copy(phone = event.phone)
            }
            is SignUpFormEvent.PasswordChanged -> {
                signUpFormState = signUpFormState.copy( password = event.password)
            }
            is SignUpFormEvent.RepeatedPasswordChanged -> {
                signUpFormState = signUpFormState.copy( repeatedPassword = event.repeatedPassword)
            }

            is SignUpFormEvent.Submit -> {
                submitData()
            }
        }
    }

    private fun submitData() {
        val firstNameResult = validateField.execute(signUpFormState.firstName)
        val lastNameResult = validateField.execute(signUpFormState.lastName)
        val emailResult = validateEmail.execute(signUpFormState.email)
        val passwordResult = validatePassword.execute(signUpFormState.password)
        val repeatedPasswordResult = validateRepeatedPassword.execute(
            signUpFormState.password, signUpFormState.repeatedPassword
        )

        val hasError = listOf(
            firstNameResult,
            lastNameResult,
            emailResult,
            passwordResult,
            repeatedPasswordResult
        ).any{ !it.isSuccessful}

        if (hasError){
            signUpFormState = signUpFormState.copy(
                firstNameError = firstNameResult.errorMessage,
                lastNameError = lastNameResult.errorMessage,
                emailError = emailResult.errorMessage,
                passwordError = passwordResult.errorMessage,
                repeatedPasswordError = repeatedPasswordResult.errorMessage
            )
            return
        }
        registerUser(
            firstName = signUpFormState.firstName,
            lastName = signUpFormState.lastName,
            email = signUpFormState.email,
            phoneNumber = signUpFormState.phone,
            password = signUpFormState.password,
            confirmPassword = signUpFormState.repeatedPassword
        )
    }

    private fun registerUser(firstName: String, lastName: String, email: String,
                             phoneNumber: String, password: String,
                             confirmPassword: String, role: String = "role_user"){

        viewModelScope.launch {
            _authState.value = _authState.value.copy(isLoading = true)

            val request = RegisterRequest(
                firstName = firstName,
                lastName = lastName,
                email = email,
                phoneNumber = phoneNumber,
                role = role,
                password = password,
                confirmPassword = confirmPassword
            )
            authRepository.registerUser(request).collect{
                withContext(Dispatchers.Main){
                    when (val result = it){
                        is Resource.Success ->{
                            _authState.value = _authState.value.copy(
                                isLoading = false,
                                error = null,
                                statusCode = it.data!!.httpStatusCode
                            )

                            registrationEventChannel.send(
                                RegistrationEvent.Success(it.data.message)
                            )
                        }

                        is Resource.Error ->{
                            _authState.value = _authState.value.copy(
                                isLoading = false,
                                error = result.message,
                                statusCode = it.data!!.httpStatusCode
                            )
                            registrationEventChannel.send(
                                RegistrationEvent.Failure(_authState.value.error!!)
                            )
                        }
                        else -> Unit
                    }
                }
            }
        }
    }

    sealed class RegistrationEvent{
        data class Success(val message: String): RegistrationEvent()
        data class Failure(val errorMessage: String): RegistrationEvent()
    }
}