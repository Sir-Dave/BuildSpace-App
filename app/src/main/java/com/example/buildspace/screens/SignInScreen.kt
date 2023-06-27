package com.example.buildspace.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.buildspace.R
import com.example.buildspace.navigation.Screen
import com.example.buildspace.ui.theme.BuildSpaceTheme

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun SignIn(navHostController: NavHostController){
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        var email by remember{ mutableStateOf("") }
        var password by remember{ mutableStateOf("") }

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
                navHostController.navigate(Screen.HomeScreen.route){
                    popUpTo(Screen.AuthScreen.route){
                        inclusive = true
                    }
                }
            },
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
        }
    }

}

@Preview(showBackground = true)
@Composable
fun SignInPreview() {
    BuildSpaceTheme {
        //SignIn()
    }
}