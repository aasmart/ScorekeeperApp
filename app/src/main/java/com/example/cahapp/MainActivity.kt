package com.example.cahapp

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.compose.animation.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.cahapp.game.types.Game
import com.example.cahapp.ui.theme.CAHAppTheme
import com.example.cahapp.ui.theme.Purple500
import com.example.cahapp.ui.theme.Purple700

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CAHAppTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Purple700
                ) {
                    AppMain()
                }
            }
        }
    }
}

@Composable
fun AppMain(appViewModel: AppViewModel = viewModel()) {
    val appUiState by appViewModel.uiState.collectAsState()
    val context = LocalContext.current

//    context.openFileOutput("games", Context.MODE_PRIVATE).use {
//        it.write(appViewModel.getGames()[0].toJson().toString().toByteArray())
//    }

    Scaffold(
        topBar = {
            TopAppBar(
                backgroundColor = Purple700,
                elevation = 8.dp
            ) {
                Row(
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    TitleText("Game Night Scorekeeper")
                }
            }
        },
        bottomBar = {
            BottomAppBar(
                backgroundColor = Purple700
            ) {

            }
        },
        floatingActionButton = {
            NewGameButton { appViewModel.toggleGameModal() }
        },
        floatingActionButtonPosition = FabPosition.Center,
        isFloatingActionButtonDocked = true
    ) {
        Box(modifier = Modifier.padding(it)) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxSize()
            ) {
                GameList(appViewModel, appUiState.gameCards)
            }
        }
    }

    if (appUiState.isCreatingGame)
        NewGameModal(appViewModel)

    AnimatedVisibility(
        visible = appUiState.isFocusedGameVisible,
        enter = slideInHorizontally() {
                maxWidth -> maxWidth / 3
        } + fadeIn(
            // Fade in with the initial alpha of 0.3f.
            initialAlpha = 0.3f
        ),
        exit = slideOutHorizontally() { maxWidth -> maxWidth / 3 } + fadeOut()
    ) {
        BackHandler(enabled = true) {
            appViewModel.setFocusedGameVisible(false)
        }
        appUiState.focusedGame?.GamePage()
    }
}

@Composable
fun TitleText(title: String) {
    Text(
        text = title,
        fontWeight = FontWeight.Bold,
        fontSize = 24.sp,
        modifier = Modifier.padding(12.dp),
        color = Color.White
    )
}

@Composable
fun ColumnScope.GameList(appViewModel: AppViewModel, games: List<Game>) {
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .weight(1f, true)
            .background(MaterialTheme.colors.background),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp),
        contentPadding = PaddingValues(12.dp)

    ) {
        items(games) { game -> game.GetAsCard(appViewModel) }
    }
}

@Composable
fun NewGameButton(addGame: () -> Unit) {
    FloatingActionButton(
        onClick = addGame,
        backgroundColor = Purple500,
    ) {
        Icon(Icons.Filled.Add, "Game")
    }
}

// TODO General cleanup, improve length function, fix keyboard overlap,
@OptIn(
    ExperimentalMaterialApi::class
)
@Composable
fun NewGameModal(appViewModel: AppViewModel) {
    val context = LocalContext.current

    // The name for the new game being created
    val gameName = remember { mutableStateOf("") }
    val playerName = remember { mutableStateOf("") }
    val players = remember { mutableStateListOf<String>() }
    val expanded = remember { mutableStateOf(false) }
    val selectedIndex = remember { mutableStateOf(0) }

    @Composable
    fun Player(name: String) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = MaterialTheme.colors.background, shape = RoundedCornerShape(8))
                .padding(4.dp)
                .clickable {
                    Toast
                        .makeText(context, "Removed '${playerName.value}'", Toast.LENGTH_SHORT)
                        .show()
                    players.remove(name)
                }
        ) {
            Text(text = name, fontSize = 18.sp, modifier = Modifier.weight(1f, true))
            Icon(Icons.Filled.Remove, "Remove player", tint = MaterialTheme.colors.error)
        }

    }

    fun isValidName(str: String): Boolean {
        return str.length in 1..24
    }

    Box(modifier = Modifier
        .fillMaxSize()
        .background(Color(0, 0, 0, 80))
        .clickable(
            interactionSource = MutableInteractionSource(),
            indication = null
        ) { appViewModel.toggleGameModal() }, contentAlignment = Alignment.Center
    ) {
        Card(
            shape = RoundedCornerShape(10.dp),
            elevation = 8.dp,
            modifier = Modifier
                .padding(30.dp, 5.dp, 30.dp, 10.dp)
                .heightIn(0.dp, 680.dp)
                .clickable(interactionSource = MutableInteractionSource(), indication = null) {},
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.padding(16.dp),
            ) {
                // Title
                Text(
                    text = stringResource(R.string.create_new_game),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Black
                )
                Divider()

                // Input fields
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {

                    TextField(
                        value = gameName.value,
                        onValueChange = { gameName.value = it },
                        label = { Text(text = stringResource(R.string.game_name)) },
                        singleLine = true,
                        isError = !isValidName(gameName.value),
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(10f, true),
                    )

                    Box(modifier = Modifier
                        .weight(10f, true)
                        .fillMaxWidth()) {
                        ExposedDropdownMenuBox(
                            expanded = expanded.value,
                            onExpandedChange = {
                                expanded.value = !expanded.value
                            }
                        ) {
                            TextField(
                                value = AppViewModel.ScoringType.values()[selectedIndex.value].readableName,
                                onValueChange = {},
                                label = { Text(text = stringResource(R.string.game_preset)) },
                                readOnly = true,
                                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded.value) },
                                modifier = Modifier.fillMaxWidth()
                            )

                            ExposedDropdownMenu(
                                expanded = expanded.value,
                                onDismissRequest = { expanded.value = false },
                                modifier = Modifier.width(IntrinsicSize.Max)
                            ) {
                                AppViewModel.ScoringType.values().forEachIndexed { index, enum ->
                                    DropdownMenuItem(onClick = {
                                        selectedIndex.value = index
                                        expanded.value = !expanded.value
                                        Toast.makeText(context, AppViewModel.ScoringType.values()[selectedIndex.value].readableName, Toast.LENGTH_SHORT).show()
                                    }) {
                                        Text(enum.readableName)
                                    }
                                }
                            }
                        }
                    }

                    // PLAYER INPUT
                    Text(text = "Players", fontSize = 18.sp, fontWeight = FontWeight.Bold, modifier = Modifier.weight(5f, true))
                    Divider()
                    Card(
                        shape = RoundedCornerShape(3.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(55f, true)
                            .height(200.dp),
                        elevation = 4.dp,
                        border = BorderStroke(1.dp, MaterialTheme.colors.background),
                    ) {
                        // PLAYER NAME LIST
                        LazyColumn(
                            verticalArrangement = Arrangement.spacedBy(6.dp),
                            contentPadding = PaddingValues(12.dp)

                        ) {
                            items(players.toList()) { player -> Player(player) }
                        }
                    }

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(10f, true)
                    ) {
                        fun addPlayerToGame() {
                            if(!isValidName(playerName.value))  {
                                Toast.makeText(context, "Name must be within valid size.", Toast.LENGTH_SHORT).show()
                                return
                            }

                            if(players.contains(playerName.value)) {
                                Toast.makeText(context, "There is already a player with this name.", Toast.LENGTH_SHORT).show()
                                return
                            }

                            players.add(playerName.value)
                            Toast.makeText(context, "Added player '${playerName.value}'", Toast.LENGTH_SHORT).show()
                            playerName.value = ""
                        }

                        TextField(
                            value = playerName.value,
                            onValueChange = { playerName.value = it },
                            label = { Text(text = stringResource(R.string.player_name_field)) },
                            singleLine = true,
                            maxLines = 1,
                            isError = !isValidName(playerName.value),
                            keyboardActions = KeyboardActions(onDone = {
                                addPlayerToGame()
                            }),
                            modifier = Modifier
                                .fillMaxSize()
                                .weight(8f, true)
                        )

                        Button(
                            onClick = { addPlayerToGame() },
                            colors = ButtonDefaults.buttonColors(backgroundColor = Purple500, contentColor = Color.White),
                            modifier = Modifier
                                .fillMaxSize()
                                .weight(2f, true)
                        ) {
                            Icon(Icons.Filled.Add, "Add Player", modifier = Modifier.fillMaxSize())
                        }
                    }

                    // Button bar
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(10f, true),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        // Cancel button
                        OutlinedButton(
                            onClick = { appViewModel.toggleGameModal() },
                            modifier = Modifier.weight(1f),
                            elevation = ButtonDefaults.elevation(defaultElevation = 2.dp)
                        ) {
                            Text(text = stringResource(R.string.cancel))
                        }

                        // Continue button
                        Button(
                            onClick = {
                                if(!isValidName(gameName.value)) {
                                    Toast.makeText(context, "Invalid game name", Toast.LENGTH_SHORT).show()
                                    return@Button
                                }

                                val minPlayers = AppViewModel.ScoringType.values()[selectedIndex.value].minPlayers
                                if(players.size < minPlayers) {
                                    Toast.makeText(context, "This game requires at least $minPlayers player ${if(minPlayers == 1) "" else "s"}", Toast.LENGTH_SHORT).show()
                                    return@Button
                                }

                                appViewModel.addNewGame(gameName.value, players.toList(), AppViewModel.ScoringType.values()[selectedIndex.value])
                            },
                            elevation = ButtonDefaults.elevation(defaultElevation = 2.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(text = stringResource(R.string.create_game))
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {

}