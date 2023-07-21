package com.example.buildspace.presentation.composables

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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.buildspace.R
import com.example.buildspace.ui.theme.BuildSpaceTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PaymentDialog(
    isSuccess: Boolean,
    text: String,
    onDismissRequest: () -> Unit,
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
                    )
                )

                Button(
                    onClick = {
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

@Preview(showBackground = true)
@Composable
fun PaymentDialogPreview(){
    BuildSpaceTheme{
        PaymentDialog(
            isSuccess = true,
            text = "Congratulations",
            onDismissRequest = {}
        )
    }
}