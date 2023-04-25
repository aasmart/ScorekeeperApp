package com.example.scorekeeper.game.types

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.example.scorekeeper.game.Round
import com.example.scorekeeper.ui.theme.Purple200
import com.example.scorekeeper.ui.theme.Purple500

open class SingleWinRoundGame(name: String, players: List<String>) : Game(name, players) {
    val rounds = mutableListOf<Round>()
    var roundDisplayCollapsed = false

    open fun scoreUpdateInteract(cardName: String) {
        updateScore(playerName = cardName, 1)
        rounds.add(Round(mapOf(Pair(cardName, 1))))
    }
}