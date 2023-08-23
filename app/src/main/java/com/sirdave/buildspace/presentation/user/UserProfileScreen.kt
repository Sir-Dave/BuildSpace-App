package com.sirdave.buildspace.presentation.user

import android.widget.Toast
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sirdave.buildspace.R
import kotlinx.coroutines.flow.Flow

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun UserProfileScreen(
    userInfo: UserInfoState,
    formState: UserFormState,
    userEvent: Flow<UserEvent>,
    onEvent: (UserInfoEvent) -> Unit,
    onDismiss: () -> Unit,
    onLogOut: () -> Unit,
    modifier: Modifier = Modifier
){
    val controller = LocalSoftwareKeyboardController.current
    val user = userInfo.user

    val context = LocalContext.current
    LaunchedEffect(Unit){
        onEvent(UserInfoEvent.LoadProfile)

        userEvent.collect{ event ->
            when (event){
                is UserEvent.Success ->
                    Toast.makeText(context, event.message, Toast.LENGTH_SHORT).show()

                is UserEvent.Failure ->
                    Toast.makeText(context, event.errorMessage, Toast.LENGTH_SHORT).show()

                is UserEvent.IsLoggedOut -> onLogOut()
            }
        }
    }

    user?.let {
        val initials  = "${it.firstName.substring(0, 1)}${it.lastName.substring(0, 1)}"

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
                    .size(100.dp)
                    .padding(8.dp)
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
                    value = formState.firstName,
                    onValueChange = { firstName ->
                        onEvent(UserInfoEvent.FirstNameChanged(firstName))
                    },
                    label = {
                        Text(text = formState.firstNameError?: stringResource(R.string.first_name))
                    },
                    placeholder = {
                        Text(text = stringResource(R.string.first_name))
                    },
                    isError = formState.firstNameError != null,
                    modifier = modifier
                        .weight(1f),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Next
                    )
                )
                Spacer(modifier = modifier.width(16.dp))

                OutlinedTextField(
                    value = formState.lastName,
                    onValueChange = { lastName ->
                        onEvent(UserInfoEvent.LastNameChanged(lastName))
                    },
                    label = {
                        Text(text = formState.lastNameError ?: stringResource(R.string.last_name))
                    },
                    placeholder = {
                        Text(text = stringResource(R.string.last_name))
                    },
                    isError = formState.lastNameError != null,
                    modifier = modifier.weight(1f),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Next
                    )
                )
            }

            OutlinedTextField(
                value = formState.email,
                onValueChange = {  },
                label = {
                    Text(text = stringResource(R.string.email))
                },
                placeholder = {
                    Text(text = stringResource(R.string.email))
                },
                readOnly = true,
                modifier = modifier
                    .fillMaxWidth()
                    .padding(start = 8.dp, end = 8.dp),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Next
                )
            )

            OutlinedTextField(
                value = formState.phoneNumber,
                onValueChange = { phone -> onEvent(UserInfoEvent.PhoneNumberChanged(phone))},
                label = {
                    Text(text = formState.phoneNumberError ?: stringResource(R.string.phone_number))
                },
                placeholder = {
                    Text(text = stringResource(R.string.phone_number))
                },
                isError = formState.phoneNumberError != null,
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
                    onEvent(UserInfoEvent.LogoutUser)
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
}