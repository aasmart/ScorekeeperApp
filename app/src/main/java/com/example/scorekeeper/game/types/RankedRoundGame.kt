package com.example.scorekeeper.game.types

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.scorekeeper.R
import com.example.scorekeeper.game.Round

class RankedRoundGame(name: String, players: List<String>) : SingleWinRoundGame(name, players) {
    val playerRoundPlacements = HashMap<String, Int>()
    val placementNumbers: List<Int>

    init {
        players.forEach { playerRoundPlacements[it] = 0 }
        placementNumbers = (1..playerRoundPlacements.size).toList()
    }

    fun finishRound(context: Context) {
        if(playerRoundPlacements.values.indexOf(0) >= 0) {
            Toast.makeText(context, "All players must have a place", Toast.LENGTH_SHORT).show()
            return
        }

        playerRoundPlacements.forEach { (player, rank) -> updateScore(player, playerRoundPlacements.size - (rank-1)) }
        rounds.add(Round(playerRoundPlacements.toMutableMap()))
        playerScores.forEach {(player, _) -> playerRoundPlacements[player] = 0 }
    }

}