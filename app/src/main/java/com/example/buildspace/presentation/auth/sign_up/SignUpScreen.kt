package com.example.buildspace.presentation.auth

import android.util.Log
import android.widget.Toast
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.buildspace.R
import com.example.buildspace.presentation.navigation.Screen
import com.example.buildspace.ui.theme.BuildSpaceTheme

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun SignUp(
    navHostController: NavHostController,
    viewModel: AuthViewModel = hiltViewModel()
){
    //val state = viewModel.authState
    val state by viewModel.authState.collectAsState()

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        var firstName by remember{ mutableStateOf("") }
        var lastName by remember{ mutableStateOf("") }
        var email by remember{ mutableStateOf("") }
        var phone by remember{ mutableStateOf("") }
        var password by remember{ mutableStateOf("") }
        var confirmPassword by remember{ mutableStateOf("") }

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
                value = firstName,
                onValueChange = { firstName = it },
                label = {
                    Text(text = stringResource(id = R.string.first_name))
                },
                placeholder = {
                    Text(text = stringResource(id = R.string.first_name))
                },
                modifier = Modifier.weight(1f),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Next
                )
            )

            Spacer(modifier = Modifier.width(16.dp))

            OutlinedTextField(
                value = lastName,
                onValueChange = { lastName = it },
                label = {
                    Text(text = stringResource(id = R.string.last_name))
                },
                placeholder = {
                    Text(text = stringResource(id = R.string.last_name))
                },
                modifier = Modifier.weight(1f),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Next
                )
            )
        }

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = {
                Text(text = stringResource(id = R.string.email))
            },
            placeholder = {
                Text(text = stringResource(id = R.string.email))
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 8.dp, end = 8.dp),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Next
            )
        )

        OutlinedTextField(
            value = phone,
            onValueChange = { phone = it },
            label = {
                Text(text = stringResource(id = R.string.phone_number))
            },
            placeholder = {
                Text(text = stringResource(id = R.string.phone_number))
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 8.dp, end = 8.dp),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Phone,
                imeAction = ImeAction.Next
            )
        )

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = {
                Text(text = stringResource(id = R.string.password))
            },
            placeholder = {
                Text(text = stringResource(id = R.string.password))
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 8.dp, end = 8.dp),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Next
            )
        )

        OutlinedTextField(
            value = confirmPassword,
            onValueChange = { confirmPassword = it },
            label = {
                Text(text = stringResource(id = R.string.confirm_password))
            },
            placeholder = {
                Text(text = stringResource(id = R.string.confirm_password))
            },
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
            )
        )

        Button(
            onClick = {
                viewModel.registerUser(
                    firstName, lastName, email,
                    phone, "role_user",
                    password, confirmPassword
                )
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

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .weight(1f),
            verticalAlignment = Alignment.Bottom,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = stringResource(id = R.string.existing_account),
                modifier = Modifier.padding(bottom = 8.dp),
                fontWeight = FontWeight.Light,
                fontSize = 15.sp,
            )

            Button(
                onClick = {
                    navHostController.navigate(Screen.SignInScreen.route)
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Black,
                    contentColor = Color.White
                )
            ) {
                Text(text = stringResource(id = R.string.login))
            }
        }

        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ){
            if (state.isLoading){
                CircularProgressIndicator()
            }
        }

        if (state.error != null){
            val context = LocalContext.current
            Toast.makeText(context, state.error, Toast.LENGTH_SHORT).show()
            Log.d("SignUpScreen", state.error!!)
        }

        else if (state.statusCode in listOf(200, 201)){
            navHostController.navigate(Screen.SignInScreen.route)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SignUpPreview() {
    BuildSpaceTheme {
        //SignUp()
    }
}