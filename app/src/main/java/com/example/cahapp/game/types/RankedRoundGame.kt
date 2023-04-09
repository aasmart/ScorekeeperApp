package com.example.cahapp.game.types

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
import com.example.cahapp.R
import com.example.cahapp.game.Round

class RankedRoundGame(name: String, players: List<String>) : SingleWinRoundGame(name, players) {
    private val playerRoundPlacements = mutableStateMapOf<String, Int>()
    private val placementNumbers: List<Int>

    init {
        players.forEach { playerRoundPlacements[it] = 0 }
        placementNumbers = (1..playerRoundPlacements.size).toList()
    }

    private fun finishRound(context: Context) {
        if(playerRoundPlacements.values.indexOf(0) >= 0) {
            Toast.makeText(context, "All players must have a place", Toast.LENGTH_SHORT).show()
            return;
        }

        playerRoundPlacements.forEach { (player, rank) -> updateScore(player, playerRoundPlacements.size - (rank-1)) }
        rounds.add(Round(playerRoundPlacements.toMutableMap()))
        playerScores.forEach {(player, _) -> playerRoundPlacements[player] = 0 }
    }

    @OptIn(ExperimentalMaterialApi::class)
    override fun LazyListScope.gamePageScoringLayout(nameSortingModalState: ModalBottomSheetState) {
        item {
            Text(text = "Current Round", fontWeight = FontWeight.Bold, fontSize = 24.sp, modifier = Modifier.padding(6.dp))
            Divider(modifier = Modifier.padding(12.dp, 0.dp, 12.dp, 16.dp))
        }

        scoreCard(nameSortingModalState)

        item {
            val context = LocalContext.current

            Button(
                onClick = { finishRound(context) },
                colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.primaryVariant, contentColor = MaterialTheme.colors.onSurface),
                modifier = Modifier.fillMaxWidth().height(48.dp)
            ) {
                Text(text = "Finish Round", fontSize = MaterialTheme.typography.button.fontSize)
            }
        }

        item {
            Text(text = "Previous Rounds", fontWeight = FontWeight.Bold, fontSize = 24.sp, modifier = Modifier.padding(6.dp))
            Divider(modifier = Modifier.padding(12.dp, 0.dp, 12.dp, 16.dp))
        }

        previousRoundDisplay()
    }

    @OptIn(ExperimentalMaterialApi::class)
    @Composable
    override fun ScoreUpdateInputs(cardName: String) {
        val expanded = remember { mutableStateOf(false) }
        val selectedIndex = remember { mutableStateOf(0) }
        val context = LocalContext.current

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
                            if(placeIndex >= 0)
                                playerRoundPlacements[playerRoundPlacements.toList()[placeIndex].first] = playerRoundPlacements[cardName]!!.toInt()

                            playerRoundPlacements[cardName] = place
                            Toast.makeText(context, "Set $cardName's place to $place", Toast.LENGTH_SHORT).show()
                        }) {
                            Text(place.toString())
                        }
                    }
                }
            }
        }
    }
}