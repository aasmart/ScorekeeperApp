package com.example.scorekeeper.game.renderers

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Divider
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ExposedDropdownMenuBox
import androidx.compose.material.ExposedDropdownMenuDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.scorekeeper.AppViewModel
import com.example.scorekeeper.R
import com.example.scorekeeper.game.players.Player
import com.example.scorekeeper.game.round.Round
import com.example.scorekeeper.game.types.RankedRoundGame
import kotlinx.coroutines.launch

class RankedRoundGameRenderer(override val game: RankedRoundGame) : RoundGameRenderer(game) {
    private suspend fun finishRound(
        appViewModel: AppViewModel,
        context: Context
    ) {
        if (game.players.any { it.rank == -1 }) {
            Toast.makeText(context, "All players must be ranked", Toast.LENGTH_SHORT).show()
            return
        }

        game.players.forEach { player ->
            updateScore(
                appViewModel,
                context,
                player,
                game.players.size - (player.rank - 1),
                false
            )
        }
        game.rounds.add(Round(game.players.map { it.copy() }))
        game.players.forEach { it.rank = -1}

        appViewModel.setActiveGame(context, game)
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

        scoringList(appViewModel, nameSortingModalState)

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
                Text(
                    text = "Finish Round",
                    fontSize = MaterialTheme.typography.button.fontSize,
                    color = Color.White
                )
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

        previousRoundDisplay()
    }

    @OptIn(ExperimentalMaterialApi::class, ExperimentalMaterialApi::class)
    @Composable
    override fun UpdateScoreInputs(
        appViewModel: AppViewModel,
        player: Player
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
                var itemDisplayValue = game.players.find { it == player }?.rank.toString()
                if (itemDisplayValue == "-1")
                    itemDisplayValue = "Unranked"

                TextField(
                    value = itemDisplayValue,
                    onValueChange = {},
                    label = { Text(text = stringResource(R.string.rank)) },
                    readOnly = true,
                    singleLine = true,
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded.value) },
                    modifier = Modifier.fillMaxWidth()
                )

                ExposedDropdownMenu(
                    expanded = expanded.value,
                    onDismissRequest = { expanded.value = false },
                    modifier = Modifier.width(IntrinsicSize.Max)
                ) {
                    game.placementNumbers.toList().forEachIndexed { index, place ->
                        DropdownMenuItem(onClick = {
                            selectedIndex.value = index
                            expanded.value = !expanded.value

                            // Check to see if there is already a player with the same placement.
                            // If there is, swap their places
                            val placePlayer = game.players.find { it.rank == place }
                            if (placePlayer != null)
                                placePlayer.rank = game.players.find { it == player }?.rank!!

                            game.players.find { it == player }?.rank = place
                            Toast.makeText(
                                context,
                                "Set ${player.name}'s place to $place",
                                Toast.LENGTH_SHORT
                            ).show()

                            scope.launch {
                                appViewModel.setActiveGame(
                                    context,
                                    game
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
}