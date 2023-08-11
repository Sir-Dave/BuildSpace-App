package com.example.buildspace.presentation

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.buildspace.R
import com.example.buildspace.presentation.user.UserInfoEvent
import com.example.buildspace.presentation.user.UserInfoState

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun ProfileScreen(
    state: UserInfoState,
    onEvent: (UserInfoEvent) -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
){
    val controller = LocalSoftwareKeyboardController.current
    val initials by remember {
        mutableStateOf("${state.firstName.substring(0, 1)}${state.lastName.substring(0, 1)}")
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Text(
            text = stringResource(id = R.string.update_profile),
            style = TextStyle(
                fontSize = 24.sp,
                lineHeight = 32.sp,
                fontWeight = FontWeight(700),
                color = Color(0xFF201B15)
            ),
            modifier = modifier
                .fillMaxWidth()
                .padding(16.dp),
            textAlign = TextAlign.Center
        )

        Box(
            modifier = modifier
                .size(100.dp).padding(8.dp)
                .border(BorderStroke(1.dp, Color.Black), shape = CircleShape)
                .background(Color.White, shape = CircleShape),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = initials,
                style = MaterialTheme.typography.bodySmall,
                textAlign = TextAlign.Center,
                color = Color.Black,
            )
        }

        Row(
            modifier = modifier
                .fillMaxWidth()
                .padding(start = 8.dp, end = 8.dp),
        ) {
            OutlinedTextField(
                value = state.firstName,
                onValueChange = { onEvent(UserInfoEvent.FirstNameChanged(it)) },
                label = {
                    Text(text = state.firstNameError?: stringResource(R.string.first_name))
                },
                placeholder = {
                    Text(text = stringResource(R.string.first_name))
                },
                isError = state.firstNameError != null,
                modifier = modifier
                    .weight(1f),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Next
                )
            )
            Spacer(modifier = modifier.width(16.dp))

            OutlinedTextField(
                value = state.lastName,
                onValueChange = { onEvent(UserInfoEvent.LastNameChanged(it)) },
                label = {
                    Text(text = state.lastNameError ?: stringResource(R.string.last_name))
                },
                placeholder = {
                    Text(text = stringResource(R.string.last_name))
                },
                isError = state.lastNameError != null,
                modifier = modifier.weight(1f),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Next
                )
            )
        }

        OutlinedTextField(
            value = state.email,
            onValueChange = {  },
            label = {
                Text(text = stringResource(R.string.email))
            },
            placeholder = {
                Text(text = stringResource(R.string.email))
            },
            modifier = modifier
                .fillMaxWidth()
                .padding(start = 8.dp, end = 8.dp),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Next
            )
        )

        OutlinedTextField(
            value = state.phoneNumber,
            onValueChange = { onEvent(UserInfoEvent.PhoneNumberChanged(it))},
            label = {
                Text(text = state.phoneNumberError ?: stringResource(R.string.phone_number))
            },
            placeholder = {
                Text(text = stringResource(R.string.phone_number))
            },
            isError = state.phoneNumberError != null,
            modifier = modifier
                .fillMaxWidth()
                .padding(start = 8.dp, end = 8.dp),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = { controller?.hide() }
            )
        )

        Button(
            onClick = {
                onDismiss()
                onEvent(UserInfoEvent.Submit)
            },
            modifier = modifier
                .fillMaxWidth()
                .padding(8.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Black,
                contentColor = Color.White
            ),
            shape = RoundedCornerShape(8.dp),

            ) {
            Text(text = stringResource(id = R.string.update_profile).uppercase())
        }

        Button(
            onClick = {
                onDismiss()
            },
            modifier = modifier
                .fillMaxWidth()
                .padding(8.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFf55347),
                contentColor = Color.White
            ),
            shape = RoundedCornerShape(8.dp),

            ) {
            Text(text = stringResource(id = R.string.sign_out).uppercase())
        }

    }

}


@Preview(name = "Profile Preview", showBackground = true)
@Composable
fun ProfilePreview(){
    ProfileScreen(
        onDismiss = {},
        state = UserInfoState(),
        onEvent = {}
    )
}