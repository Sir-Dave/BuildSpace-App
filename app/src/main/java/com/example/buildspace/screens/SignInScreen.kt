package com.example.buildspace.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.buildspace.R
import com.example.buildspace.ui.theme.BuildSpaceTheme

@Composable
fun SignIn(){
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        var email by remember{ mutableStateOf("") }
        var password by remember{ mutableStateOf("") }

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
                .padding(8.dp)
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
                .padding(8.dp)
        )

        Button(
            onClick = {},
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

        Button(
            onClick = {},
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Black,
                contentColor = Color.White
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
                color = Color.Black
            )

            Button(
                onClick = {},
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
        SignIn()
    }
}