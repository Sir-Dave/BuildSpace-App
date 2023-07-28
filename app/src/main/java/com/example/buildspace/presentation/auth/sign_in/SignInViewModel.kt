package com.example.buildspace.presentation.auth.sign_in

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.buildspace.data.local.AuthManager
import com.example.buildspace.data.mapper.toUser
import com.example.buildspace.data.remote.dto.request.SignInRequest
import com.example.buildspace.domain.repository.AuthRepository
import com.example.buildspace.domain.use_cases.ValidateEmail
import com.example.buildspace.domain.use_cases.ValidateField
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
class SignInViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val authManager: AuthManager,
    private val validateField: ValidateField,
    private val validateEmail: ValidateEmail,
): ViewModel() {

    private val _authState = MutableStateFlow(AuthState())
    val authState: StateFlow<AuthState> = _authState

    var signInFormState by mutableStateOf(SignInFormState())

    private val signInEventChannel = Channel<SignInEvent>()
    val validationEvent = signInEventChannel.receiveAsFlow()

    fun onEvent(event: SignInFormEvent){
        when(event){
            is SignInFormEvent.EmailChanged -> {
                signInFormState = signInFormState.copy( email = event.email)
            }

            is SignInFormEvent.PasswordChanged -> {
                signInFormState = signInFormState.copy( password = event.password)
            }

            is SignInFormEvent.RememberMeChanged -> {
                signInFormState = signInFormState.copy(isRememberUser = event.isChecked)
            }

            is SignInFormEvent.Submit -> {
                submitData()
            }
        }
    }

    private fun submitData() {
        val emailResult = validateEmail.execute(signInFormState.email)
        val passwordResult = validateField.execute(signInFormState.password)

        val hasError = listOf(
            emailResult,
            passwordResult
        ).any{ !it.isSuccessful}

        if (hasError){
            signInFormState = signInFormState.copy(
                emailError = emailResult.errorMessage,
                passwordError = passwordResult.errorMessage

            )
            return
        }

        loginUser(
            signInFormState.email,
            signInFormState.password
        )
    }

    private fun loginUser(email: String, password: String){
        viewModelScope.launch {
            _authState.value = _authState.value.copy(isLoading = true)

            val request = SignInRequest(email, password)
            authRepository.signInUser(request).collect{
                withContext(Dispatchers.Main){
                    when (val result = it){
                        is Resource.Success ->{
                            _authState.value = _authState.value.copy(
                                isLoading = false,
                                error = null,
                                token = result.data!!.token,
                                user =  result.data.user.toUser()
                            )

                            authManager.saveToken(_authState.value.token!!)
                            authManager.saveUser(_authState.value.user!!)

                            if (signInFormState.isRememberUser) {
                                authManager.saveUserLoginState()
                            }

                            signInEventChannel.send(SignInEvent.Success)
                        }

                        is Resource.Error ->{
                            _authState.value = _authState.value.copy(
                                isLoading = false,
                                error = result.message,
                                token = null,
                                user = null
                            )
                            signInEventChannel.send(
                                SignInEvent.Failure(_authState.value.error!!)
                            )
                        }
                        else -> Unit
                    }
                }
            }
        }
    }

    sealed class SignInEvent{
        object Success: SignInEvent()
        data class Failure(val errorMessage: String): SignInEvent()
    }
}