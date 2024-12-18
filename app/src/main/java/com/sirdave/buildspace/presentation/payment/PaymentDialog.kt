package com.sirdave.buildspace.presentation.payment

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.sirdave.buildspace.R

@Composable
fun PaymentDialog(
    isSuccess: Boolean,
    text: String,
    onDismissRequest: () -> Unit,
    onNavigateToDashboard: () -> Unit
){
    Dialog(onDismissRequest = { onDismissRequest() }) {
        OutlinedCard(
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier.padding(8.dp),
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp, Alignment.Top),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Image(
                    painter = if (isSuccess)
                        painterResource(id = R.drawable.check_circle_48)
                    else painterResource(id = R.drawable.close_48),
                    contentDescription = "image description",
                    contentScale = ContentScale.None,
                    modifier = Modifier.padding(8.dp)
                        .height(70.dp).width(70.dp)
                )

                Text(
                    text = text,
                    style = TextStyle(
                        fontSize = 16.sp,
                        lineHeight = 24.sp,
                        fontWeight = FontWeight(500),
                        color = Color(0xFF000000),
                        letterSpacing = 0.15.sp,
                    ),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(start = 8.dp, end = 8.dp),
                    maxLines = 2
                )

                Button(
                    onClick = {
                        if (isSuccess){
                            onNavigateToDashboard()
                        }
                        onDismissRequest()
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Black,
                        contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                ) {
                    Text(text = stringResource(id = R.string.text_continue).uppercase())
                }
            }
        }
    }
}