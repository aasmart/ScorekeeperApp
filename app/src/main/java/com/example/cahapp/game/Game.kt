package com.example.cahapp.game

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.example.cahapp.AppViewModel
import com.example.cahapp.R
import com.example.cahapp.TitleText
import com.example.cahapp.ui.theme.Purple500
import com.example.cahapp.ui.theme.Purple700
import org.json.JSONObject
import java.lang.Integer.min

class Game(var name: String, players: List<String>, val type: ScoringType) : java.io.Serializable {
    val playerScores = mutableStateMapOf<String, Int>();
    val isComplete = mutableStateOf(false);
    private var appViewModel: AppViewModel? = null;
    private val podiumPlaces =  arrayOf(PodiumPlace.SECOND, PodiumPlace.FIRST, PodiumPlace.THIRD);

    init {
        players.forEach { name ->
            playerScores[name] = 0
        }
    }

    enum class ScoringType(val readableName: String) {
        SIMPLE_SCORING("Simple Scoring"),
        ROUNDS("Round Scoring")
    }

    enum class PodiumPlace(val rankingInt: Int, val colorId: Int) {
        FIRST(1, R.color.gold),
        SECOND(2, R.color.silver),
        THIRD(3, R.color.bronze)
    }

    @OptIn(ExperimentalFoundationApi::class)
    @Composable
    fun GetAsCard(appViewModel: AppViewModel) {
        this.appViewModel = appViewModel;

        var expanded by remember { mutableStateOf(false) }

        Card(
            shape = RoundedCornerShape(10),
            elevation = 10.dp,
            modifier = Modifier
                //.border(2.dp, Purple700, RoundedCornerShape(10))
                .height(240.dp)
                .fillMaxWidth()
                .width(IntrinsicSize.Max)
                .height(IntrinsicSize.Max)
                .combinedClickable(
                    onClick = {
                        this.appViewModel?.setFocusedGame(this)
                        this.appViewModel?.setFocusedGameVisible(true)
                    },
                    onLongClick = { expanded = true }
                )
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
                        Text(text = (if (isComplete.value) "Finished" else "In Progress"))
                    }
                    Column() {
                        Text(text = "Leaderboard:", fontWeight = FontWeight.Black)
                    }
                }

                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    DropdownMenuItem(onClick = {
                        appViewModel.removeGame(this@Game)
                        expanded = false
                    }) {
                        Text("Delete")
                    }
                }
            }
        }
    }

    @Composable
    fun GamePage() {
        @Composable
        fun TopBar() {
            TopAppBar(
                backgroundColor = Purple700,
                elevation = 8.dp,
            ) {
                Box(modifier = Modifier.fillMaxSize()) {
                    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxSize()) {
                        IconButton(onClick = { appViewModel?.setFocusedGameVisible(false) }, modifier = Modifier.size(32.dp)) {
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
                        IconButton(onClick = { /* TODO: implement hamburger menu */ }, modifier = Modifier.size(32.dp)) {
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
        }

        fun LazyListScope.determineLayout() {
            when (type) {
                ScoringType.SIMPLE_SCORING -> {
                    if(isComplete.value)
                        return;

                    item {
                        Text(text = "Scoring", fontWeight = FontWeight.Bold, fontSize = 24.sp, modifier = Modifier.padding(6.dp))
                        Divider(modifier = Modifier.padding(12.dp, 0.dp, 12.dp, 16.dp))
                    }
                    scoreCard()
                }
                ScoringType.ROUNDS -> TODO()
            }
        }

        Scaffold(
            topBar = { TopBar() },
            floatingActionButton = {
                if(!isComplete.value)
                    FinishGameButton()
            },
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(10.dp),
                contentPadding = PaddingValues(8.dp, 8.dp)
            ) {
                determineLayout()

                item {
                    Text(text = "Leaderboard", fontWeight = FontWeight.Bold, fontSize = 24.sp, modifier = Modifier.padding(6.dp))
                    Divider(modifier = Modifier.padding(12.dp, 0.dp, 12.dp, 16.dp))
                }

                leaderboard()
            }
        }
    }

    @Composable
    fun FinishGameButton() {
        FloatingActionButton(
            onClick = {  isComplete.value = true },
            backgroundColor = Purple500,
        ) {
            Icon(Icons.Filled.Check, "Game")
        }
    }

    private fun LazyListScope.scoreCard() {
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
                Text(
                    text = "PLAYER",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .zIndex(1f)
                )
                Text(
                    text = "SCORE",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .zIndex(1f)
                )
            }
        }
        items(playerScores.keys.toList()) { player -> PlayerCard(cardName = player) }
    }

    private fun updateScore(playerName: String, amount: Int = 1) {
        playerScores.replace(playerName, playerScores.getValue(playerName) + amount)
    }

    @Composable
    fun PlayerCard(cardName: String) {
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
                            .weight(1f, true)
                            .background(Purple500)
                            .fillMaxHeight()
                    )
                    Text(
                        text = cardName,
                        modifier = Modifier
                            .weight(10f, true)
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
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colors.background)
                        .border(0.75.dp, MaterialTheme.colors.background, RoundedCornerShape(10))
                ) {
                    Button(
                        onClick = { updateScore(playerName = cardName, -1) },
                        colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.surface),
                        border = BorderStroke(0.dp, MaterialTheme.colors.background),
                        shape = RectangleShape,
                        modifier = Modifier
                            .fillMaxHeight()
                            .weight(1f, true)
                    ) {
                        Text(text = "-", fontWeight = FontWeight.Black, fontSize = 24.sp, color = MaterialTheme.colors.onSurface)
                    }
                    Button(
                        onClick = { updateScore(playerName = cardName) },
                        colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.surface),
                        border = BorderStroke(0.dp, MaterialTheme.colors.background),
                        shape = RectangleShape,
                        modifier = Modifier
                            .fillMaxHeight()
                            .weight(1f, true)
                    ) {
                        Text(text = "+", fontWeight = FontWeight.Black, fontSize = 24.sp, color = MaterialTheme.colors.onSurface)
                    }
                }
            }
        }
    }

    @Composable
    private fun PlayerLeaderboardCard(cardName: String, rank: Int) {
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
                    text = playerScores.getValue(cardName).toString(),
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
    private fun RowScope.podium(players: MutableMap<String, Int>, placeOrder: Array<PodiumPlace>) {
        val maxHeight = 0.85f;
        val minHeight = 0.5f;
        val shadowMax = 8.0f;
        val shadowMin = 2.0f;

        val localDensity = LocalDensity.current;

        val weight = 1.0f / placeOrder.size
        val heightDecrease = (maxHeight - minHeight) / (placeOrder.size - 1)
        val shadowDecrease = (shadowMax - shadowMin) / (placeOrder.size - 1)
        val playerNames = players.keys.toList();

        // Print out the places
        for (place in placeOrder) {
            val rankIndex = place.rankingInt;

            if(playerNames.size <= rankIndex - 1)
                continue;

            val playerName = playerNames[rankIndex - 1]
            Box(
                contentAlignment = Alignment.BottomCenter,
                modifier = Modifier
                    .weight(weight)
                    .fillMaxHeight()
                    .zIndex(-rankIndex.toFloat())
            ) {
                var podiumHeight by remember { mutableStateOf(0.dp) }

                Text(
                    text = playerName,
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
                            .fillMaxHeight(maxHeight - (rankIndex - 1) * heightDecrease)
                            .shadow((shadowMax - shadowDecrease * (rankIndex - 1)).dp)
                            .background(
                                color = colorResource(PodiumPlace.values()[rankIndex - 1].colorId),
                                RoundedCornerShape(4)
                            )
                            .onGloballyPositioned { pos ->
                                podiumHeight = with(localDensity) { pos.size.height.toDp() }
                            }
                    ) {
                        Text(text = players[playerName].toString(), fontWeight = FontWeight.Black, color = Purple500, modifier = Modifier
                            .padding(16.dp))
                    }
                }
            }
        }
    }

    private fun LazyListScope.leaderboard() {
        val sortedPlayers = playerScores.toList().sortedByDescending { (_, value) -> value }.toMap().toMutableMap()

        item {
            Box(modifier = Modifier
                .offset(0.dp, 3.dp)
                .fillMaxWidth()
                .height(120.dp), contentAlignment = Alignment.BottomCenter
            ) {
                Row(modifier = Modifier
                    .fillMaxSize()
                    .padding(start = 24.dp, end = 24.dp, top = 20.dp, bottom = 0.dp), verticalAlignment = Alignment.Bottom) {
                    podium(sortedPlayers, podiumPlaces);
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

        itemsIndexed(sortedPlayers.keys.toList().subList(min(podiumPlaces.size, sortedPlayers.size), sortedPlayers.size)) { index, player ->
            run {
                PlayerLeaderboardCard(cardName = player, rank = podiumPlaces.size + index + 1)
            }
        }
    }

    fun toJson(): JSONObject {
        val json = JSONObject()
        json.put("name", name)

        return json
    }
}