package com.example.buildspace.presentation.auth.sign_in

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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.buildspace.R
import com.example.buildspace.presentation.navigation.Screen

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun SignIn(
    navHostController: NavHostController,
    viewModel: SignInViewModel = hiltViewModel()
){
    val state by viewModel.authState.collectAsState()
    val loginState = viewModel.signInFormState
    val context = LocalContext.current

    LaunchedEffect(key1 = context){
        viewModel.validationEvent.collect{ event ->
            when (event){
                is SignInViewModel.SignInEvent.Success ->{
                    navHostController.navigate(Screen.HomeScreen.route) {
                        popUpTo(Screen.AuthScreen.route) {
                            inclusive = true
                        }
                    }
                }
                is SignInViewModel.SignInEvent.Failure -> {
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
                viewModel.onEvent(SignInFormEvent.EmailChanged(it))
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
                viewModel.onEvent(SignInFormEvent.PasswordChanged(it))
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
            visualTransformation = PasswordVisualTransformation()
        )

        Button(
            onClick = { viewModel.onEvent(SignInFormEvent.Submit) },
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
                    viewModel.onEvent(SignInFormEvent.RememberMeChanged(it))
                },
            )

            Spacer(modifier = Modifier.width(8.dp))

            Text(stringResource(id = R.string.remember_user))
        }
        
        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = stringResource(id = R.string.no_account),
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            style = MaterialTheme.typography.bodyMedium,
            fontSize = 15.sp,
            textAlign = TextAlign.Center
        )

        Text(
            text = stringResource(id = R.string.register),
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .clickable {
                    navHostController.navigate(Screen.SignUpScreen.route)
                },
            style = MaterialTheme.typography.bodyMedium,
            fontSize = 15.sp,
            textAlign = TextAlign.Center
        )


        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ){
            if (state.isLoading){
                CircularProgressIndicator()
            }
        }

        /*Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .weight(1f),
            verticalAlignment = Alignment.Bottom,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = stringResource(id = R.string.no_account),
                modifier = Modifier.padding(bottom = 8.dp),
                fontWeight = FontWeight.Light,
                fontSize = 15.sp,
            )

            Button(
                onClick = {
                    navHostController.navigate(Screen.SignUpScreen.route)
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Black,
                    contentColor = Color.White
                )
            ) {
                Text(text = stringResource(id = R.string.register))
            }
        }*/
    }
}