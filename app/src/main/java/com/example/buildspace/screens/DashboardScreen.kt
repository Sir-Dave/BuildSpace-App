package com.example.buildspace.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.buildspace.ui.theme.BuildSpaceTheme
import com.example.buildspace.ui.theme.Pink50

@Composable
fun Dashboard(){
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        val name by remember{ mutableStateOf("David") }
        val initials by remember{ mutableStateOf("DV") }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ){
            Text(text = "Welcome back $name", modifier = Modifier.padding(8.dp))
            CircularText(
                text = initials,
                backgroundColor = Pink50 ,
                borderColor = Color.Black,
                modifier = Modifier
            )
        }

        Column(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = "02/28",
                style = MaterialTheme.typography.displayLarge,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
        }
    }

}

@Composable
fun CircularText(text: String,
                 backgroundColor: Color,
                 borderColor: Color,
                 modifier: Modifier) {
    Box(
        modifier = modifier.padding(8.dp),
        contentAlignment = Alignment.Center,
    ) {
        Box(
            modifier = modifier
                .size(30.dp)
                .border(BorderStroke(1.dp, borderColor), shape = CircleShape)
                .background(backgroundColor, shape = CircleShape),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = text,
                style = MaterialTheme.typography.bodySmall,
                textAlign = TextAlign.Center,
                color = Color.Black,
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
fun DashboardPreview(){
    BuildSpaceTheme{
        Dashboard()
    }
}
