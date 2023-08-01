package com.example.buildspace.presentation.auth.sign_up

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.buildspace.R
import com.example.buildspace.presentation.auth.AuthState
import kotlinx.coroutines.flow.Flow

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun SignUp(
    state: AuthState,
    signUpState: SignUpFormState,
    registrationEvent: Flow<RegistrationEvent>,
    onEvent: (SignUpFormEvent) -> Unit,
    onNavigateToSignInScreen: () -> Unit
){
    val context = LocalContext.current

    var isPasswordVisible by remember { mutableStateOf(false) }
    var isRepeatedPasswordVisible by remember { mutableStateOf(false) }

    LaunchedEffect(key1 = context){
        registrationEvent.collect{ event ->
            when (event){
                is RegistrationEvent.Success ->{
                    Toast.makeText(
                        context,
                        event.message,
                        Toast.LENGTH_SHORT).show()

                    onNavigateToSignInScreen()

                }

                is RegistrationEvent.Failure -> {
                    Toast.makeText(
                        context,
                        event.errorMessage,
                        Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        val controller = LocalSoftwareKeyboardController.current

        Text(
            text = stringResource(id = R.string.signup_header),
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 32.dp, start = 8.dp, end = 8.dp),
            style = MaterialTheme.typography.headlineMedium,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold
        )

        Text(
            text = stringResource(id = R.string.signup_subtitle),
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp, start = 8.dp, end = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            OutlinedTextField(
                value = signUpState.firstName,
                onValueChange = { onEvent(SignUpFormEvent.FirstNameChanged(it)) },
                label = {
                    if (signUpState.firstNameError != null){
                        Text(
                            text = signUpState.firstNameError,
                            fontSize = 12.sp
                        )
                    }
                    else {
                        Text(text = stringResource(id = R.string.first_name))
                    }

                },
                placeholder = {
                    Text(text = stringResource(id = R.string.first_name))
                },
                isError = signUpState.firstNameError != null,
                modifier = Modifier.weight(1f),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Next
                )
            )

            Spacer(modifier = Modifier.width(16.dp))

            OutlinedTextField(
                value = signUpState.lastName,
                onValueChange = { onEvent(SignUpFormEvent.LastNameChanged(it)) },
                label = {
                    if (signUpState.lastNameError != null){
                        Text(
                            text = signUpState.lastNameError,
                            fontSize = 12.sp
                        )
                    }
                    else {
                        Text(text = stringResource(id = R.string.last_name))
                    }

                },
                placeholder = {
                    Text(text = stringResource(id = R.string.last_name))
                },
                isError = signUpState.lastNameError != null,
                modifier = Modifier.weight(1f),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Next
                )
            )
        }

        OutlinedTextField(
            value = signUpState.email,
            onValueChange = { onEvent(SignUpFormEvent.EmailChanged(it)) },
            label = {
                Text(text = signUpState.emailError ?: stringResource(id = R.string.email))
            },
            placeholder = {
                Text(text = stringResource(id = R.string.email))
            },
            isError = signUpState.emailError != null,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 8.dp, end = 8.dp),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Next
            )
        )

        OutlinedTextField(
            value = signUpState.phone,
            onValueChange = { onEvent(SignUpFormEvent.PhoneChanged(it)) },
            label = {
                Text(text = signUpState.phoneError ?: stringResource(id = R.string.phone_number))
            },
            placeholder = {
                Text(text = stringResource(id = R.string.phone_number))
            },
            isError = signUpState.phoneError != null,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 8.dp, end = 8.dp),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Phone,
                imeAction = ImeAction.Next
            )
        )

        OutlinedTextField(
            value = signUpState.password,
            onValueChange = { onEvent(SignUpFormEvent.PasswordChanged(it)) },
            label = {
                Text(text = signUpState.passwordError ?: stringResource(id = R.string.password))
            },
            placeholder = {
                Text(text = stringResource(id = R.string.password))
            },
            isError = signUpState.passwordError != null,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 8.dp, end = 8.dp),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Next
            ),
            trailingIcon = {
                IconButton(
                    onClick = { isPasswordVisible = !isPasswordVisible }
                ) {
                    val visiblePasswordIcon = painterResource(id = R.drawable.visibility_on)
                    val hiddenPasswordIcon = painterResource(id = R.drawable.visibility_off)
                    val passwordVisibilityIcon = if (isPasswordVisible)
                        visiblePasswordIcon else hiddenPasswordIcon

                    Icon(painter = passwordVisibilityIcon, contentDescription = null)
                }
            },
            visualTransformation = if (isPasswordVisible)
                VisualTransformation.None else PasswordVisualTransformation()
        )

        OutlinedTextField(
            value = signUpState.repeatedPassword,
            onValueChange = { onEvent(SignUpFormEvent.RepeatedPasswordChanged(it)) },
            label = {
                Text(text = signUpState.repeatedPasswordError ?: stringResource(id = R.string.confirm_password))
            },
            placeholder = {
                Text(text = stringResource(id = R.string.confirm_password))
            },
            isError = signUpState.repeatedPasswordError != null,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 8.dp, end = 8.dp),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    controller?.hide()
                }
            ),
            trailingIcon = {
                IconButton(
                    onClick = { isRepeatedPasswordVisible = !isRepeatedPasswordVisible }
                ) {
                    val visiblePasswordIcon = painterResource(id = R.drawable.visibility_on)
                    val hiddenPasswordIcon = painterResource(id = R.drawable.visibility_off)
                    val passwordVisibilityIcon = if (isRepeatedPasswordVisible)
                        visiblePasswordIcon else hiddenPasswordIcon

                    Icon(painter = passwordVisibilityIcon, contentDescription = null)
                }
            },
            visualTransformation = if (isRepeatedPasswordVisible)
                VisualTransformation.None else PasswordVisualTransformation()
        )

        Button(
            onClick = {
                onEvent(SignUpFormEvent.Submit)
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Black,
                contentColor = Color.White
            ),

        ) {
            Text(text = stringResource(id = R.string.register))
        }

        Divider(
            modifier = Modifier.padding(
                start = 24.dp,
                end = 24.dp,
                top = 8.dp,
                bottom = 8.dp
            )
        )

        OutlinedButton(
            onClick = { },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            colors = ButtonDefaults.outlinedButtonColors(
                containerColor = Color.Transparent,
                contentColor = Color.Black
            )
        ) {
            Text(text = stringResource(id = R.string.sign_up_with_google))
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth().padding(8.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ){
            Text(
                text = stringResource(id = R.string.existing_account)
            )

            Text(
                text = stringResource(id = R.string.login),
                modifier = Modifier.padding(start = 8.dp)
                    .clickable { onNavigateToSignInScreen() },
                color = Color(0xFF00639A)
            )
        }

        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ){
            if (state.isLoading){
                CircularProgressIndicator()
            }
        }
    }
}