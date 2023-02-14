package com.example.cahapp.game

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.example.cahapp.ui.theme.Purple200
import com.example.cahapp.ui.theme.Purple500
import com.example.cahapp.ui.theme.Purple700

class Game(var name: String, players: List<String>, val isComplete: Boolean) {
    val playerScores: Map<String, Int> = players.associateBy({it}, {0});

    @Composable
    fun GetAsCard() {
        Card(
            shape = RoundedCornerShape(10),
            elevation = 10.dp,
            modifier = Modifier
                .padding(12.dp, 12.dp, 12.dp, 0.dp)
                //.border(2.dp, Purple700, RoundedCornerShape(10))
                .height(240.dp)
                .fillMaxWidth()
                .width(IntrinsicSize.Max)
                .height(IntrinsicSize.Max)
                .clickable { }
        ) {
            Column() {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .background(Purple500)
                        .fillMaxWidth()
                        .height(45.dp)
                        .shadow(1.dp)
                ) {
                    Text(
                        text = name,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .zIndex(1f)
                            .padding(6.dp, 0.dp, 6.dp, 0.dp)
                    )
                }

                Column(
                    modifier = Modifier.padding(vertical = 6.dp, horizontal = 12.dp)
                ) {
                    Row() {
                        Text(text = "Game Status: ", fontWeight = FontWeight.Black)
                        Text(text = (if (isComplete) "Finished" else "In Progress"))
                    }
                    Column() {
                        Text(text = "Leaderboard:", fontWeight = FontWeight.Black)
                    }
                }
            }
        }
    }

    @Composable
    fun GetAsPage() {
        Scaffold(
            topBar = {
                TopAppBar(

                ) {

                }
            }
        ) {

        }
    }
}