package com.example.scorekeeper.game.types

import android.annotation.SuppressLint
import android.content.Context
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Sort
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
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
import com.example.scorekeeper.TitleText
import com.example.scorekeeper.game.Player
import com.example.scorekeeper.ui.theme.Purple500
import com.example.scorekeeper.ui.theme.Purple700
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import org.json.JSONObject
import java.lang.Integer.min

@Serializable
sealed class Game {
    abstract var name: String
    abstract val players: List<Player>
    abstract var playerSortOrder: SortingOrder
    abstract var isComplete: Boolean
}

enum class SortingOrder(val readableName: String) {
    ALPHABETICAL(readableName = "Alphabetical Order"),
    REVERSE_ALPHABETICAL(readableName = "Reverse Alphabetical Order")
}

/*protected suspend fun updateScore(
    appViewModel: AppViewModel,
    context: Context,
    playerName: String,
    amount: Int = 1
) {
    playerScores.replace(playerName, playerScores.getValue(playerName) + amount)
    appViewModel.setActiveGame(context, this)
}*/

/*
fun setPlayers(players: List<String>) {
    players.forEach { name ->
        playerScores[name] = 0
    }
}

private fun sortPlayerNames(): Map<String, Int> {
    return when (playerSortOrder) {
        SortingOrder.ALPHABETICAL -> playerScores.toList().sortedBy { (name, _) -> name }
            .toMap()

        SortingOrder.REVERSE_ALPHABETICAL -> playerScores.toList()
            .sortedByDescending { (name, _) -> name }.toMap()
    }
}

    @OptIn(ExperimentalFoundationApi::class)
    @Composable
    fun GetAsCard(appViewModel: AppViewModel) {

    }

    @Composable
    private fun GameTopAppBar(appViewModel: AppViewModel) {
        val scope = rememberCoroutineScope()
        val context = LocalContext.current

        Box(modifier = Modifier.fillMaxSize()) {
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxSize()) {
                IconButton(onClick = {
                    scope.launch {
                        appViewModel.setActiveGame(context, null)
                    }
                }, modifier = Modifier.size(32.dp)) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.White,
                        modifier = Modifier.size(32.dp)
                    )
                }
            }

            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxSize()
            ) {
                TitleText(name)
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.End,
                modifier = Modifier.fillMaxSize()
            ) {
                IconButton(
                    onClick = { */
/* TODO: implement hamburger menu *//*
 },
                    modifier = Modifier.size(32.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.Menu,
                        contentDescription = "Menu",
                        tint = Color.White,
                        modifier = Modifier.size(32.dp)
                    )
                }
            }
        }
    }

    @Composable
    private fun FinishGameButton(appViewModel: AppViewModel) {
        val scope = rememberCoroutineScope()
        val context = LocalContext.current

        FloatingActionButton(
            onClick = {
                scope.launch {
                    isComplete = true
                    appViewModel.setActiveGame(context, this@Game)
                }
            },
            backgroundColor = MaterialTheme.colors.primaryVariant,
        ) {
            Icon(Icons.Filled.Check, "Game", tint = MaterialTheme.colors.onSurface)
        }
    }

    @OptIn(ExperimentalMaterialApi::class)
    protected open fun LazyListScope.gamePageScoringLayout(
        appViewModel: AppViewModel,
        nameSortingModalState: ModalBottomSheetState
    ) {
        item {
            Text(
                text = "Scoring",
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp,
                modifier = Modifier.padding(6.dp)
            )
            Divider(modifier = Modifier.padding(12.dp, 0.dp, 12.dp, 16.dp))
        }

        scoreCard(appViewModel, nameSortingModalState)
    }

    @SuppressLint("UnusedMaterialScaffoldPaddingParameter")
    @OptIn(ExperimentalMaterialApi::class)
    @Composable
    fun GamePage(appViewModel: AppViewModel) {
        @Composable
        fun TopBar() {
            TopAppBar(
                backgroundColor = Purple700,
                elevation = 8.dp,
            ) {
                GameTopAppBar(appViewModel)
            }
        }

        val nameSortingModalState = rememberModalBottomSheetState(
            ModalBottomSheetValue.Hidden
        )

        Scaffold(
            topBar = { TopBar() },
            floatingActionButton = {
                if (!isComplete)
                    FinishGameButton(appViewModel)
            },
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(10.dp),
                contentPadding = PaddingValues(8.dp, 8.dp)
            ) {
                if (!isComplete)
                    gamePageScoringLayout(appViewModel, nameSortingModalState)

                item {
                    Text(
                        text = "Leaderboard",
                        fontWeight = FontWeight.Bold,
                        fontSize = 24.sp,
                        modifier = Modifier.padding(6.dp)
                    )
                    Divider(modifier = Modifier.padding(12.dp, 0.dp, 12.dp, 16.dp))
                }

                leaderboard()
            }
        }

        NameSortBottomModal(appViewModel, nameSortingModalState)
    }

    @Composable
    private fun SortTypeForm(appViewModel: AppViewModel) {
        val scope = rememberCoroutineScope()
        val context = LocalContext.current

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
        ) {
            SortingOrder.values().forEach {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .selectable(
                            selected = it == playerSortOrder,
                            onClick = {
                                scope.launch {
                                    playerSortOrder = it
                                    appViewModel.setActiveGame(context, this@Game)
                                }
                            }
                        )
                ) {
                    RadioButton(
                        selected = (it == playerSortOrder),
                        onClick = {
                            scope.launch {
                                playerSortOrder = it
                                appViewModel.setActiveGame(context, this@Game)
                            }
                        }
                    )
                    Text(
                        text = it.readableName,
                        style = MaterialTheme.typography.body1.merge(),
                        modifier = Modifier.padding(start = 16.dp)
                    )
                }
            }
        }
    }

    @OptIn(ExperimentalMaterialApi::class)
    @Composable
    private fun NameSortBottomModal(
        appViewModel: AppViewModel,
        nameSortingModalState: ModalBottomSheetState
    ) {
        ModalBottomSheetLayout(
            sheetState = nameSortingModalState,
            sheetContent = {
                SortTypeForm(appViewModel)
            }
        ) {}
    }

    @OptIn(ExperimentalMaterialApi::class)
    protected fun LazyListScope.scoreCard(
        appViewModel: AppViewModel,
        nameSortingModalState: ModalBottomSheetState
    ) {
        item {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier
                    .background(Purple500, RoundedCornerShape(8))
                    .fillMaxWidth()
                    .height(45.dp)
                    .shadow(1.dp)
            ) {
                val coroutineScope = rememberCoroutineScope()

                Icon(Icons.Filled.Sort, "Sort Players", modifier = Modifier
                    .weight(10f)
                    .clickable {
                        coroutineScope.launch {
                            if (nameSortingModalState.isVisible)
                                nameSortingModalState.hide()
                            else
                                nameSortingModalState.show()
                        }
                    })
                Text(
                    text = "Player",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .zIndex(1f)
                        .weight(55f)
                )
                Text(
                    text = "Score",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .zIndex(1f)
                        .weight(35f)
                )
            }
        }

        items(sortPlayerNames().keys.toList()) { player ->
            PlayerCard(
                appViewModel,
                
                cardName = player
            )
        }
    }

    @Composable
    protected open fun ScoreUpdateInputs(appViewModel: AppViewModel, cardName: String) {
        val scope = rememberCoroutineScope()
        val context = LocalContext.current

        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colors.background)
                .border(0.75.dp, MaterialTheme.colors.background, RoundedCornerShape(10))
        ) {
            Button(
                onClick = { scope.launch { updateScore(appViewModel, context, playerName = cardName, -1) } },
                colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.surface),
                border = BorderStroke(0.dp, MaterialTheme.colors.background),
                shape = RectangleShape,
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(1f, true)
            ) {
                Text(
                    text = "-",
                    fontWeight = FontWeight.Black,
                    fontSize = 24.sp,
                    color = MaterialTheme.colors.onSurface
                )
            }

            Button(
                onClick = { scope.launch { updateScore(appViewModel, context, playerName = cardName) } },
                colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.surface),
                border = BorderStroke(0.dp, MaterialTheme.colors.background),
                shape = RectangleShape,
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(1f, true)
            ) {
                Text(
                    text = "+",
                    fontWeight = FontWeight.Black,
                    fontSize = 24.sp,
                    color = MaterialTheme.colors.onSurface
                )
            }
        }
    }

    @Composable
    fun PlayerCard(appViewModel: AppViewModel, cardName: String) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp)
        ) {
            Card(
                shape = RoundedCornerShape(10),
                elevation = 4.dp,
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(8f, true)
                    .padding(0.dp, 0.dp, 8.dp, 0.dp)
                    .border(0.75.dp, MaterialTheme.colors.background, RoundedCornerShape(10))
            ) {
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    Box(
                        modifier = Modifier
                            .weight(3f, true)
                            .background(Purple500)
                            .fillMaxHeight()
                    )
                    Text(
                        text = cardName,
                        modifier = Modifier
                            .weight(14f, true)
                            .fillMaxWidth()
                            .padding(12.dp, 0.dp),
                        fontSize = 24.sp,
                        textAlign = TextAlign.Start
                    )
                    Text(
                        text = playerScores.getValue(cardName).toString(),
                        modifier = Modifier
                            .weight(8f, true)
                            .padding(12.dp, 0.dp),
                        fontWeight = FontWeight.Black,
                        fontSize = 24.sp,
                        textAlign = TextAlign.End
                    )
                }
            }

            // Scoring buttons
            Card(
                shape = RoundedCornerShape(10),
                elevation = 8.dp,
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(3f, true)
            ) {
                ScoreUpdateInputs(appViewModel, cardName)
            }
        }
    }

    private fun LazyListScope.leaderboard() {
        val sortedPlayers =
            playerScores.toList().sortedByDescending { (_, value) -> value }.toMap().toMutableMap()

        item {
            Box(
                modifier = Modifier
                    .offset(0.dp, 3.dp)
                    .fillMaxWidth()
                    .height(120.dp), contentAlignment = Alignment.BottomCenter
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(start = 24.dp, end = 24.dp, top = 20.dp, bottom = 0.dp),
                    verticalAlignment = Alignment.Bottom
                ) {
                    Podium(sortedPlayers, podiumPlaces)
                }
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier
                    .background(Purple500, RoundedCornerShape(8))
                    .fillMaxWidth()
                    .height(45.dp)
                    .shadow(1.dp)
            ) {
                Text(
                    text = "Rank",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .zIndex(1f)
                )
                Text(
                    text = "Player",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .zIndex(1f)
                )
                Text(
                    text = "Score",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .zIndex(1f)
                )
            }
        }

        val nonPodiumPlayers = sortedPlayers.keys.toList()
            .subList(min(podiumPlaces.size, sortedPlayers.size), sortedPlayers.size)
        itemsIndexed(nonPodiumPlayers) { index, player ->
            run {
                PlayerLeaderboardCard(cardName = player, rank = podiumPlaces.size + index + 1)
            }
        }
    }
}
*/
