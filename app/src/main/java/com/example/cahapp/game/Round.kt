package com.example.cahapp.game

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cahapp.ui.theme.Purple500

class Round(private val winners: Array<String>) {
    @Composable
    fun GetRoundCard(roundIndex: Int) {
        Card(
            shape = RoundedCornerShape(10),
            elevation = 4.dp,
            modifier = Modifier
                .height(60.dp)
                .fillMaxWidth()
                .padding(0.dp, 0.dp, 0.dp, 0.dp)
                .border(0.75.dp, MaterialTheme.colors.background, RoundedCornerShape(10))
        ) {
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxSize()
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .weight(2f, true)
                        .background(Purple500)
                        .fillMaxHeight()
                ) {
                    Text(
                        text = roundIndex.toString(),
                        fontWeight = FontWeight.Black,
                        fontSize = 24.sp,
                    )
                }
                Text(
                    text = winners.joinToString(separator = ", "),
                    modifier = Modifier
                        .weight(10f, true)
                        .fillMaxWidth()
                        .padding(10.dp, 0.dp),
                    fontSize = 24.sp,
                    textAlign = TextAlign.Start
                )
            }
        }
    }
}