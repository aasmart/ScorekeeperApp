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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.example.scorekeeper.AppViewModel
import com.example.scorekeeper.game.Round
import com.example.scorekeeper.ui.theme.Purple200
import com.example.scorekeeper.ui.theme.Purple500

open class SingleWinRoundGame(name: String, players: List<String>) : Game(name, players) {
    var rounds = mutableListOf<Round>()
    internal var roundDisplayCollapsed = false

    override fun copy(): SingleWinRoundGame {
        val game = SingleWinRoundGame(name, listOf())
        game.playerScores = playerScores
        game.playerSortOrder = playerSortOrder
        game.isComplete = isComplete
        game.rounds = rounds
        game.roundDisplayCollapsed = roundDisplayCollapsed

        return game
    }

    open fun scoreUpdateInteract(appViewModel: AppViewModel, cardName: String) {
        updateScore(appViewModel, playerName = cardName, 1)
        rounds.add(Round(mapOf(Pair(cardName, 1))))
        appViewModel.setActiveGame(this)
    }

    fun LazyListScope.previousRoundDisplay(appViewModel: AppViewModel) {
        item {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier
                    .background(Purple500, RoundedCornerShape(8))
                    .fillMaxWidth()
                    .height(45.dp)
                    .shadow(1.dp)
                    .clickable {
                        roundDisplayCollapsed = !roundDisplayCollapsed
                        appViewModel.setActiveGame(this@SingleWinRoundGame)
                    }
            ) {
                Text(
                    text = "Round #",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .zIndex(1f)
                )
                Text(
                    text = "Placements",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .zIndex(1f)
                )
            }
        }

        // TODO proper collapsed visuals
        if(!roundDisplayCollapsed) {
            itemsIndexed(rounds) { index, round ->
                round.GetRoundCard(roundIndex = index + 1)
            }
        }
    }

    @OptIn(ExperimentalMaterialApi::class)
    override fun LazyListScope.gamePageScoringLayout(appViewModel: AppViewModel, nameSortingModalState: ModalBottomSheetState) {
        item {
            Text(text = "Current Round", fontWeight = FontWeight.Bold, fontSize = 24.sp, modifier = Modifier.padding(6.dp))
            Divider(modifier = Modifier.padding(12.dp, 0.dp, 12.dp, 16.dp))
        }

        scoreCard(appViewModel, nameSortingModalState)

        item {
            Text(text = "Previous Rounds", fontWeight = FontWeight.Bold, fontSize = 24.sp, modifier = Modifier.padding(6.dp))
            Divider(modifier = Modifier.padding(12.dp, 0.dp, 12.dp, 16.dp))
        }

        previousRoundDisplay(appViewModel)
    }

    @Composable
    override fun ScoreUpdateInputs(appViewModel: AppViewModel, cardName: String) {
        Button(
            onClick = { scoreUpdateInteract(appViewModel, cardName) },
            colors = ButtonDefaults.buttonColors(backgroundColor = Purple200),
            border = BorderStroke(0.dp, MaterialTheme.colors.background),
            shape = RectangleShape,
            modifier = Modifier
                .fillMaxSize()
        ) {
            Text(text = "Winner", fontWeight = FontWeight.Black, fontSize = 20.sp, color = MaterialTheme.colors.onSurface)
        }
    }
}