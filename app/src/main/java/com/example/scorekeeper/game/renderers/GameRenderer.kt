package com.example.scorekeeper.game.renderers

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.example.scorekeeper.AppViewModel
import com.example.scorekeeper.R
import com.example.scorekeeper.game.Player
import com.example.scorekeeper.game.types.Game
import com.example.scorekeeper.ui.theme.Purple500
import kotlinx.coroutines.launch

object GameRenderer {
    enum class PodiumPlace(val rankingInt: Int, val colorId: Int) {
        FIRST(1, R.color.gold),
        SECOND(2, R.color.silver),
        THIRD(3, R.color.bronze)
    }

    private val podiumPlaces: Array<PodiumPlace> =
        arrayOf(PodiumPlace.SECOND, PodiumPlace.FIRST, PodiumPlace.THIRD)

    @OptIn(ExperimentalFoundationApi::class)
    @Composable
    fun Card(game: Game, appViewModel: AppViewModel) {
        var expanded by remember { mutableStateOf(false) }
        val scope = rememberCoroutineScope()
        val context = LocalContext.current

        Card(
            shape = RoundedCornerShape(10),
            elevation = 10.dp,
            modifier = Modifier
                //.border(2.dp, Purple700, RoundedCornerShape(10))
                .height(250.dp)
                .fillMaxWidth()
                .width(IntrinsicSize.Max)
                .height(IntrinsicSize.Max)
                .combinedClickable(
                    onClick = { scope.launch { appViewModel.setActiveGame(context, game) } },
                    onLongClick = { expanded = true }
                )
        ) {
            Column {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .background(Purple500)
                        .fillMaxWidth()
                        .height(45.dp)
                        .shadow(1.dp)
                ) {
                    Text(
                        text = game.name,
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
                    Row {
                        Text(text = "Game Status: ", fontWeight = FontWeight.Black)
                        Text(text = (if (game.isComplete) "Finished" else "In Progress"))
                    }
                }

                val sortedPlayers = game.players.sortedByDescending { player -> player.score }
                Column(
                    modifier = Modifier
                        .padding(start = 0.dp, end = 0.dp, bottom = 0.dp, top = 0.dp)
                        .fillMaxSize()
                        .clipToBounds()
                ) {
                    Row(
                        modifier = Modifier
                            .weight(96f)
                            .padding(start = 20.dp, end = 20.dp, top = 36.dp, bottom = 0.dp)
                            .offset(y = 5.dp),
                        verticalAlignment = Alignment.Bottom
                    ) {
                        Podium(sortedPlayers, podiumPlaces)
                    }
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(4f)
                            .clip(RoundedCornerShape(2.dp))
                            .background(MaterialTheme.colors.primaryVariant)
                            .zIndex(10f)
                            .shadow(6.dp)
                    )
                }

                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    DropdownMenuItem(onClick = {
                        scope.launch {
                            appViewModel.removeGame(context, game)
                            expanded = false
                        }
                    }) {
                        Text("Delete")
                    }
                }
            }
        }
    }

    @Composable
    private fun PlayerLeaderboardCard(game: Game, cardName: String, rank: Int) {
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
                        text = rank.toString(),
                        fontWeight = FontWeight.Black,
                        fontSize = 24.sp,
                    )
                }
                Text(
                    text = cardName,
                    modifier = Modifier
                        .weight(10f, true)
                        .fillMaxWidth()
                        .padding(10.dp, 0.dp),
                    fontSize = 24.sp,
                    textAlign = TextAlign.Start
                )
                Text(
                    text = (game.players.find { p -> p.name == cardName }?.score ?: 0).toString(),
                    modifier = Modifier
                        .weight(6f, true)
                        .padding(12.dp, 0.dp),
                    fontWeight = FontWeight.Black,
                    fontSize = 24.sp,
                    textAlign = TextAlign.End
                )
            }
        }
    }

    @Composable
    private fun RowScope.Podium(
        players: List<Player>,
        placeOrder: Array<PodiumPlace>,
        podiumMaxHeight: Float = 0.85f,
        podiumMinHeight: Float = 0.5f,
        podiumShadowMax: Float = 8.0f,
        podiumShadowMin: Float = 2.0f
    ) {
        val localDensity = LocalDensity.current

        val weight = 1.0f / placeOrder.size
        val heightDecrease = (podiumMaxHeight - podiumMinHeight) / (placeOrder.size - 1)
        val shadowDecrease = (podiumShadowMax - podiumShadowMin) / (placeOrder.size - 1)

        // Print out the places
        for (place in placeOrder) {
            val rankIndex = place.rankingInt

            if (players.size <= rankIndex - 1)
                continue

            val player = players[rankIndex - 1]
            Box(
                contentAlignment = Alignment.BottomCenter,
                modifier = Modifier
                    .weight(weight)
                    .fillMaxHeight()
                    .zIndex(-rankIndex.toFloat())
            ) {
                var podiumHeight by remember { mutableStateOf(0.dp) }

                Text(
                    text = player.name,
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Black,
                    modifier = Modifier
                        .padding(4.dp)
                        .offset(y = -podiumHeight)
                )

                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.BottomCenter
                ) {
                    Box(
                        contentAlignment = Alignment.BottomCenter,
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight(podiumMaxHeight - (rankIndex - 1) * heightDecrease)
                            .shadow((podiumShadowMax - shadowDecrease * (rankIndex - 1)).dp)
                            .background(
                                color = colorResource(PodiumPlace.values()[rankIndex - 1].colorId),
                                RoundedCornerShape(4)
                            )
                            .onGloballyPositioned { pos ->
                                podiumHeight = with(localDensity) { pos.size.height.toDp() }
                            }
                    ) {
                        Text(
                            text = player.score.toString(),
                            fontWeight = FontWeight.Black,
                            color = Purple500,
                            fontSize = 16.sp,
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                }
            }
        }
    }
}