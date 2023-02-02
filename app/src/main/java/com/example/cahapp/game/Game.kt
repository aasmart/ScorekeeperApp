package com.example.cahapp.game

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.example.cahapp.ui.theme.Purple200
import com.example.cahapp.ui.theme.Purple700

class Game(var name: String, players: List<String>) {
    val playerScores: Map<String, Int> = players.associateBy({it}, {0});

    @Composable
    fun GetAsCard() {
        Box(
            modifier = Modifier
                .padding(12.dp, 12.dp, 12.dp, 0.dp)
                .clickable { }
        ) {
            Text(
                text = name,
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .offset(10.dp, (-10).dp)
                    .zIndex(1f)
                    .clip(RoundedCornerShape(25))
                    .background(MaterialTheme.colors.background)
                    .padding(6.dp, 0.dp, 6.dp, 0.dp)
            )

            Column(
                modifier = Modifier
                    .clip(RoundedCornerShape(15))
                    .background(Purple200)
                    .border(4.dp, Purple700)
                    .fillMaxWidth()
                    .height(74.dp)
                    .padding(12.dp , 18.dp, 12.dp, 0.dp)
            ) {
                Row() {
                    Column() {
                        Text(text = "Rounds:", fontWeight = FontWeight.Bold)
                        Text(text = "0")
                    }
                    Column(modifier = Modifier.padding(start = 12.dp)) {
                        Text(text = "Players:", fontWeight = FontWeight.Bold)
                        Row() {
                            playerScores.forEach { player ->
                                Text(
                                    text = player.key + ":" + player.value,
                                    modifier = Modifier.padding(end = 12.dp)
                                )
                            }
                        }
                    }

                }

            }
        }

    }
}