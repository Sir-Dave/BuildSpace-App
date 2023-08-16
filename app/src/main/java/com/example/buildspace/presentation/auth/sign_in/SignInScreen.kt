package com.example.buildspace.presentation.auth.sign_in

import android.widget.Toast
import androidx.activity.compose.BackHandler
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
import com.example.buildspace.R
import com.example.buildspace.presentation.auth.AuthState
import kotlinx.coroutines.flow.Flow

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun SignIn(
    state: AuthState,
    loginState: SignInFormState,
    validationEvent: Flow<SignInEvent>,
    onEvent: (SignInFormEvent) -> Unit,
    onNavigateToHomeScreen: () -> Unit,
    onNavigateToSignUpScreen: () -> Unit,
    onExitApp: () -> Unit
){
    val context = LocalContext.current
    var isPasswordVisible by remember { mutableStateOf(false) }

    LaunchedEffect(key1 = context){
        validationEvent.collect{ event ->
            when (event){
                is SignInEvent.Success ->{ onNavigateToHomeScreen() }
                is SignInEvent.Failure -> {
                    Toast.makeText(
                        context,
                        event.errorMessage,
                        Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    BackHandler(enabled = true) {
        onExitApp()
    }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        val controller = LocalSoftwareKeyboardController.current

        Text(
            text = stringResource(id = R.string.login_header),
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 32.dp, start = 8.dp, end = 8.dp),
            style = MaterialTheme.typography.headlineMedium,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold
        )

        Text(
            text = stringResource(id = R.string.login_subtitle),
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center
        )

        OutlinedTextField(
            value = loginState.email,
            onValueChange = {
                onEvent(SignInFormEvent.EmailChanged(it))
            },
            label = {
                Text(
                    text = loginState.emailError ?: stringResource(id = R.string.email)
                )
            },
            placeholder = {
                Text(text = stringResource(id = R.string.email))
            },
            isError = loginState.emailError != null,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 8.dp, end = 8.dp),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Next
            )
        )

        OutlinedTextField(
            value = loginState.password,
            onValueChange = {
                onEvent(SignInFormEvent.PasswordChanged(it))
            },
            label = {
                Text(
                    text = loginState.passwordError ?: stringResource(id = R.string.password)
                )
            },

            placeholder = {
                Text(text = stringResource(id = R.string.password))
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 8.dp, end = 8.dp),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done
            ),
            isError = loginState.passwordError != null,
            keyboardActions = KeyboardActions(
                onDone = {
                    controller?.hide()
                }
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

        Button(
            onClick = { onEvent(SignInFormEvent.Submit) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Black,
                contentColor = Color.White
            ),

        ) {
            Text(text = stringResource(id = R.string.login))
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
            onClick = {},
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            colors = ButtonDefaults.outlinedButtonColors(
                containerColor = Color.Transparent,
                contentColor = Color.Black
            )
        ) {
            Text(text = stringResource(id = R.string.sign_in_with_google))
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ){
            Switch(
                checked = loginState.isRememberUser,
                onCheckedChange = {
                    onEvent(SignInFormEvent.RememberMeChanged(it))
                },
            )

            Spacer(modifier = Modifier.width(8.dp))

            Text(stringResource(id = R.string.remember_user))
        }
        
        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ){
            Text(
                text = stringResource(id = R.string.no_account),
            )

            Text(
                text = stringResource(id = R.string.register),
                modifier = Modifier
                    .padding(start = 8.dp)
                    .clickable { onNavigateToSignUpScreen() },
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