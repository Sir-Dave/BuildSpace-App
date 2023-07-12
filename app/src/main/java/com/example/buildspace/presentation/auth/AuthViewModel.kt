package com.example.buildspace.presentation.auth

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.buildspace.data.local.TokenManager
import com.example.buildspace.data.mapper.toUser
import com.example.buildspace.data.remote.dto.request.RegisterRequest
import com.example.buildspace.data.remote.dto.request.SignInRequest
import com.example.buildspace.domain.repository.AuthRepository
import com.example.buildspace.domain.use_cases.ValidateEmail
import com.example.buildspace.domain.use_cases.ValidatePassword
import com.example.buildspace.presentation.LoginFormEvent
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
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val tokenManager: TokenManager,
    private val validateEmail: ValidateEmail,
    private val validatePassword: ValidatePassword,
): ViewModel() {

    private val token = MutableStateFlow<String?>(null)

    private val _authState = MutableStateFlow(AuthState())
    val authState: StateFlow<AuthState> get() = _authState


    init {
        viewModelScope.launch(Dispatchers.IO) {
            tokenManager.getToken().collect {
                withContext(Dispatchers.Main) {
                    token.value = it
                }
            }
        }
    }

    var loginFormState by mutableStateOf(LoginFormState())
    private val validationEventChannel = Channel<ValidationEvent>()
    val validationEvent = validationEventChannel.receiveAsFlow()

    fun onEvent(event: LoginFormEvent){
        when(event){
            is LoginFormEvent.EmailChanged -> {
                loginFormState = loginFormState.copy( email = event.email)
            }

            is LoginFormEvent.PasswordChanged -> {
                loginFormState = loginFormState.copy( password = event.password)
            }

            is LoginFormEvent.Submit -> {
                submitData()
            }
        }
    }

    private fun submitData() {
        val emailResult = validateEmail.execute(loginFormState.email)
        val passwordResult = validatePassword.execute(loginFormState.password)

        val hasError = listOf(
            emailResult,
            passwordResult
        ).any{ !it.isSuccessful}

        if (hasError){
            loginFormState = loginFormState.copy(
                emailError = emailResult.errorMessage,
                passwordError = passwordResult.errorMessage

            )
            return
        }
        viewModelScope.launch {
            validationEventChannel.send(ValidationEvent.Success)
        }
//        loginUser(
//            loginFormState.email,
//            loginFormState.password
//        )
    }

    fun registerUser(firstName: String, lastName: String, email: String,
                     phoneNumber: String, role: String, password: String,
                     confirmPassword: String){

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
                        }

                        is Resource.Error ->{
                            _authState.value = _authState.value.copy(
                                isLoading = false,
                                error = result.message,
                                statusCode = it.data!!.httpStatusCode
                            )
                        }
                        else -> Unit
                    }
                }
            }
        }
    }

    fun loginUser(email: String, password: String){
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
                                user = result.data.user.toUser()
                            )
                            saveToken(_authState.value.token!!)
                        }

                        is Resource.Error ->{
                            _authState.value = _authState.value.copy(
                                isLoading = false,
                                error = result.message,
                                token = null,
                                user = null
                            )
                        }
                        else -> Unit
                    }
                }
            }
        }
    }

    private fun saveToken(token: String) {
        viewModelScope.launch(Dispatchers.IO) {
            tokenManager.saveToken(token)
        }
    }

    fun deleteToken() {
        viewModelScope.launch(Dispatchers.IO) {
            tokenManager.deleteToken()
        }
    }

    sealed class ValidationEvent{
        object Success: ValidationEvent()
    }
}