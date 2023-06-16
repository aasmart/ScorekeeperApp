package com.example.scorekeeper.game.renderers

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
import com.example.scorekeeper.TitleText
import com.example.scorekeeper.game.PodiumPlace
import com.example.scorekeeper.game.SortingOrder
import com.example.scorekeeper.game.players.AbstractPlayer
import com.example.scorekeeper.game.types.AbstractGame
import com.example.scorekeeper.ui.theme.Purple500
import com.example.scorekeeper.ui.theme.Purple700
import kotlinx.coroutines.launch
import java.lang.Integer.min

abstract class AbstractGameRenderer {
    abstract val game: AbstractGame

    companion object {
        private val podiumPlaces: Array<PodiumPlace> =
            arrayOf(PodiumPlace.SECOND, PodiumPlace.FIRST, PodiumPlace.THIRD)
    }

    protected suspend fun updateScore(
        appViewModel: AppViewModel,
        context: Context,
        player: AbstractPlayer,
        amount: Int = 1,
        recompose: Boolean = true
    ) {
        player.score += amount
        if (recompose)
            appViewModel.setActiveGame(context, game)
    }

    private fun sortPlayerNames(): List<AbstractPlayer> {
        return when (game.playerSortOrder) {
            SortingOrder.ALPHABETICAL -> game.players.sortedBy { p -> p.name }
            SortingOrder.REVERSE_ALPHABETICAL -> game.players.sortedByDescending { p -> p.name }
        }
    }

    @Composable
    private fun RowScope.Podium(
        players: List<AbstractPlayer>,
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

    @OptIn(ExperimentalFoundationApi::class)
    @Composable
    fun Card(appViewModel: AppViewModel) {
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
                        color = Color.White,
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
    private fun GameTopAppBar(appViewModel: AppViewModel) {
        val scope = rememberCoroutineScope()
        val context = LocalContext.current

        Box(modifier = Modifier.fillMaxSize()) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxSize()
            ) {
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
                TitleText(game.name)
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.End,
                modifier = Modifier.fillMaxSize()
            ) {
                IconButton(
                    onClick = {},
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
        ExtendedFloatingActionButton(
            text = { Text("Finish game", color = Color.White) },
            onClick = {
                appViewModel.setFinishGameAlertDialogVisible(true)
            },
            backgroundColor = Purple500,
            icon = { Icon(Icons.Filled.Check, "Finish game", tint = Color.White) }
        )
    }

    @Composable
    abstract fun UpdateScoreInputs(appViewModel: AppViewModel, player: AbstractPlayer)

    @Composable
    private fun PlayerCard(appViewModel: AppViewModel, player: AbstractPlayer) {
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
                        text = player.name,
                        modifier = Modifier
                            .weight(14f, true)
                            .fillMaxWidth()
                            .padding(12.dp, 0.dp),
                        fontSize = 24.sp,
                        textAlign = TextAlign.Start
                    )
                    Text(
                        text = player.score.toString(),
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
                UpdateScoreInputs(appViewModel, player)
            }
        }
    }

    @Composable
    internal fun HeaderText(name: String) {
        Text(
            text = name,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            modifier = Modifier
                .zIndex(1f)
        )
    }

    @Composable
    internal fun RowScope.HeaderText(name: String, weight: Float) {
        Text(
            text = name,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            modifier = Modifier
                .zIndex(1f)
                .weight(weight)
        )
    }

    @OptIn(ExperimentalMaterialApi::class)
    internal fun LazyListScope.scoringList(
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

                Icon(
                    Icons.Filled.Sort,
                    "Sort Players",
                    tint = Color.White,
                    modifier = Modifier
                        .weight(10f)
                        .clickable {
                            coroutineScope.launch {
                                if (nameSortingModalState.isVisible)
                                    nameSortingModalState.hide()
                                else
                                    nameSortingModalState.show()
                            }
                        })
                HeaderText(name = "Player", 55f)
                HeaderText(name = "Score", 55f)
            }
        }

        items(sortPlayerNames()) { player ->
            PlayerCard(
                appViewModel,
                player = player
            )
        }
    }

    @OptIn(ExperimentalMaterialApi::class)
    abstract fun LazyListScope.gamePageScoringLayout(
        appViewModel: AppViewModel,
        nameSortingModalState: ModalBottomSheetState
    )

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
                            selected = it == game.playerSortOrder,
                            onClick = {
                                scope.launch {
                                    game.playerSortOrder = it
                                    appViewModel.setActiveGame(context, game)
                                }
                            }
                        )
                ) {
                    RadioButton(
                        selected = (it == game.playerSortOrder),
                        onClick = {
                            scope.launch {
                                game.playerSortOrder = it
                                appViewModel.setActiveGame(context, game)
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

    @Composable
    private fun FinishGameAlertDialog(appViewModel: AppViewModel) {
        val context = LocalContext.current
        val scope = rememberCoroutineScope()

        AlertDialog(
            onDismissRequest = { appViewModel.setFinishGameAlertDialogVisible(false) },
            title = { Text(text = "Finish Game", fontWeight = FontWeight.Bold) },
            text = { Text(text = "Are you sure you want to finish this game?") },
            buttons = {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(
                        12.dp,
                        Alignment.CenterHorizontally
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp, 0.dp, 24.dp, 16.dp)
                ) {
                    Button(
                        onClick = {
                            scope.launch {
                                game.isComplete = true
                                appViewModel.setActiveGame(context, game)
                                appViewModel.setFinishGameAlertDialogVisible(false)
                            }
                        },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(text = "Finish Game")
                    }

                    OutlinedButton(
                        onClick = {
                            appViewModel.setFinishGameAlertDialogVisible(false)
                        },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(text = "Cancel")
                    }
                }
            },
            backgroundColor = MaterialTheme.colors.primarySurface,
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier.shadow(24.dp)
        )
    }

    @SuppressLint("UnusedMaterialScaffoldPaddingParameter")
    @OptIn(ExperimentalMaterialApi::class)
    @Composable
    fun GamePage(appViewModel: AppViewModel, isFinishGameAlertDialogOpen: Boolean) {
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
            bottomBar = {
                BottomAppBar(backgroundColor = Purple700) { }
            },
            floatingActionButton = {
                if (!game.isComplete)
                    FinishGameButton(appViewModel)
            },
            isFloatingActionButtonDocked = true,
            floatingActionButtonPosition = FabPosition.Center
        ) {
            if (isFinishGameAlertDialogOpen)
                FinishGameAlertDialog(appViewModel = appViewModel)

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(it),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(10.dp),
                contentPadding = PaddingValues(8.dp, 8.dp)
            ) {
                if (!game.isComplete)
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
    private fun PlayerLeaderboardCard(player: AbstractPlayer, rank: Int) {
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
                        color = Color.White
                    )
                }
                Text(
                    text = player.name,
                    modifier = Modifier
                        .weight(10f, true)
                        .fillMaxWidth()
                        .padding(10.dp, 0.dp),
                    fontSize = 24.sp,
                    textAlign = TextAlign.Start
                )
                Text(
                    text = player.score.toString(),
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

    private fun LazyListScope.leaderboard() {
        val sortedPlayers =
            game.players.toList().sortedByDescending { it.score }

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
                HeaderText(name = "Rank")
                HeaderText(name = "Player")
                HeaderText(name = "Score")
            }
        }

        val nonPodiumPlayers =
            sortedPlayers.subList(min(podiumPlaces.size, sortedPlayers.size), sortedPlayers.size)
        itemsIndexed(nonPodiumPlayers) { index, player ->
            run {
                PlayerLeaderboardCard(player = player, rank = podiumPlaces.size + index + 1)
            }
        }
    }
}