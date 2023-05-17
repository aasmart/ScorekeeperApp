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
import com.example.scorekeeper.AppViewModel
import com.example.scorekeeper.R
import com.example.scorekeeper.game.Player
import com.example.scorekeeper.game.Round
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable

data class RankedRoundGame(
    override var name: String,
    override val players: List<Player>,
    override var playerSortOrder: SortingOrder,
    override var isComplete: Boolean,
    var rounds: MutableList<Round>,
    var playerRoundPlacements: MutableMap<Player, Int>
) : Game() {
    companion object Factory {
        fun new(name: String, playerNames: List<String>): RankedRoundGame {
            val players = Player.toPlayerList(playerNames)
            return RankedRoundGame(
                name,
                players,
                SortingOrder.ALPHABETICAL,
                false,
                mutableListOf(),
                players.associateWith { 0 }.toMutableMap()
            )
        }
    }
}

/*@Serializable
class RankedRoundGame(override var name: String) : Game() {
    private var rounds = mutableListOf<Round>()
    private var roundDisplayCollapsed = false
    private var playerRoundPlacements = mutableMapOf<String, Int>()
    private var placementNumbers: List<Int>

    init {
        placementNumbers = listOf()
    }

    override fun setPlayers(players: List<String>) {
        super.setPlayers(players)
        players.forEach { playerRoundPlacements[it] = 0 }
        placementNumbers = (1..playerRoundPlacements.size).toList()
    }

    override fun copy(): RankedRoundGame {
        val game = RankedRoundGame(name)
        game.name = name
        game.playerScores = playerScores
        game.playerSortOrder = playerSortOrder
        game.isComplete = isComplete
        game.rounds = rounds
        game.roundDisplayCollapsed = roundDisplayCollapsed
        game.playerRoundPlacements = playerRoundPlacements
        game.placementNumbers = placementNumbers

        return game
    }

    private suspend fun finishRound(
        appViewModel: AppViewModel,
        context: Context
    ) {
        if (playerRoundPlacements.values.indexOf(0) >= 0) {
            Toast.makeText(context, "All players must have a place", Toast.LENGTH_SHORT).show()
            return
        }

        playerRoundPlacements.forEach { (player, rank) ->
            updateScore(
                appViewModel,
                context,
                player,
                playerRoundPlacements.size - (rank - 1)
            )
        }
        rounds.add(Round(playerRoundPlacements.toMutableMap()))
        playerScores.forEach { (player, _) -> playerRoundPlacements[player] = 0 }

        appViewModel.setActiveGame(context, this)
    }

    @OptIn(ExperimentalMaterialApi::class)
    override fun LazyListScope.gamePageScoringLayout(
        appViewModel: AppViewModel,
        nameSortingModalState: ModalBottomSheetState
    ) {
        item {


            Text(
                text = "Current Round",
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp,
                modifier = Modifier.padding(6.dp)
            )
            Divider(modifier = Modifier.padding(12.dp, 0.dp, 12.dp, 16.dp))
        }

        scoreCard(appViewModel, nameSortingModalState)

        item {
            val context = LocalContext.current
            val scope = rememberCoroutineScope()

            Button(
                onClick = { scope.launch { finishRound(appViewModel, context) } },
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = MaterialTheme.colors.primaryVariant,
                    contentColor = MaterialTheme.colors.onSurface
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
            ) {
                Text(text = "Finish Round", fontSize = MaterialTheme.typography.button.fontSize)
            }
        }

        item {
            Text(
                text = "Previous Rounds",
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp,
                modifier = Modifier.padding(6.dp)
            )
            Divider(modifier = Modifier.padding(12.dp, 0.dp, 12.dp, 16.dp))
        }

        previousRoundDisplay(appViewModel)
    }

    @OptIn(ExperimentalMaterialApi::class)
    @Composable
    override fun ScoreUpdateInputs(
        appViewModel: AppViewModel,
        cardName: String
    ) {
        val expanded = remember { mutableStateOf(false) }
        val selectedIndex = remember { mutableStateOf(0) }
        val context = LocalContext.current
        val scope = rememberCoroutineScope()

        Box(modifier = Modifier.fillMaxSize()) {
            ExposedDropdownMenuBox(
                expanded = expanded.value,
                onExpandedChange = {
                    expanded.value = !expanded.value
                },
                modifier = Modifier.fillMaxSize()
            ) {
                TextField(
                    value = playerRoundPlacements[cardName].toString(),
                    onValueChange = {},
                    label = { Text(text = stringResource(R.string.rank)) },
                    readOnly = true,
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded.value) },
                    modifier = Modifier.fillMaxWidth()
                )

                ExposedDropdownMenu(
                    expanded = expanded.value,
                    onDismissRequest = { expanded.value = false },
                    modifier = Modifier.width(IntrinsicSize.Max)
                ) {
                    placementNumbers.toList().forEachIndexed { index, place ->
                        DropdownMenuItem(onClick = {
                            selectedIndex.value = index
                            expanded.value = !expanded.value

                            // Check to see if there is already a player with the same placement.
                            // If there is, swap their places
                            val placeIndex = playerRoundPlacements.values.indexOf(place)
                            if (placeIndex >= 0)
                                playerRoundPlacements[playerRoundPlacements.toList()[placeIndex].first] =
                                    playerRoundPlacements[cardName]!!.toInt()

                            playerRoundPlacements[cardName] = place
                            Toast.makeText(
                                context,
                                "Set $cardName's place to $place",
                                Toast.LENGTH_SHORT
                            ).show()

                            scope.launch {
                                appViewModel.setActiveGame(
                                    context,
                                    this@RankedRoundGame
                                )
                            }
                        }) {
                            Text(place.toString())
                        }
                    }
                }
            }
        }
    }
}*/
