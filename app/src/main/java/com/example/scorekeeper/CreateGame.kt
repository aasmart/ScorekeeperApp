package com.example.scorekeeper

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.relocation.BringIntoViewRequester
import androidx.compose.foundation.relocation.bringIntoViewRequester
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ExposedDropdownMenuBox
import androidx.compose.material.ExposedDropdownMenuDefaults
import androidx.compose.material.FabPosition
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Remove
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusEvent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.scorekeeper.listeners.rememberImeState
import com.example.scorekeeper.ui.theme.Purple500
import com.example.scorekeeper.ui.theme.Purple700
import kotlinx.coroutines.launch

class CreateGameForm(private val appViewModel: AppViewModel) {
    private val gameName = mutableStateOf("")
    private val playerName = mutableStateOf("")
    private val players = mutableStateListOf<String>()
    private val selectedIndex = mutableStateOf(0)

    companion object {
        private val INPUT_FIELD_HEIGHT = 56.dp
        private val PLAYER_LIST_HEIGHT = 300.dp

        fun isValidLength(str: String, min: Int, max: Int = Int.MAX_VALUE): Boolean {
            return str.length in min..max
        }
    }

    @Composable
    private fun TopAppBar() {
        TopAppBar(
            backgroundColor = Purple700,
            elevation = 8.dp,
        ) {
            Box(modifier = Modifier.fillMaxSize()) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxSize()
                ) {
                    IconButton(
                        onClick = { appViewModel.toggleGameModal() },
                        modifier = Modifier.size(32.dp)
                    ) {
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
                    TitleText(stringResource(R.string.create_new_game))
                }
            }
        }
    }

    @Composable
    private fun FinishGameButton() {
        val context = LocalContext.current
        val scope = rememberCoroutineScope()

        FloatingActionButton(
            onClick = {
                if (!isValidLength(gameName.value, 1, 24)) {
                    Toast.makeText(context, "Invalid game name", Toast.LENGTH_SHORT).show()
                    return@FloatingActionButton
                }

                val minPlayers = AppViewModel.ScoringType.values()[selectedIndex.value].minPlayers
                if (players.size < minPlayers) {
                    Toast.makeText(
                        context,
                        "This game requires at least $minPlayers player ${if (minPlayers == 1) "" else "s"}",
                        Toast.LENGTH_SHORT
                    ).show()
                    return@FloatingActionButton
                }

                scope.launch {
                    appViewModel.addNewGame(
                        context,
                        gameName.value,
                        players.toList(),
                        AppViewModel.ScoringType.values()[selectedIndex.value]
                    )
                }
            },
            backgroundColor = Purple500,
        ) {
            Icon(Icons.Filled.Check, "Game")
        }
    }

    @OptIn(
        ExperimentalMaterialApi::class
    )
    @Composable
    private fun GameTypeDropdown() {
        val context = LocalContext.current

        val expanded = remember { mutableStateOf(false) }

        ExposedDropdownMenuBox(
            expanded = expanded.value,
            onExpandedChange = {
                expanded.value = !expanded.value
            },
            modifier = Modifier.height(INPUT_FIELD_HEIGHT)
        ) {
            TextField(
                value = AppViewModel.ScoringType.values()[selectedIndex.value].readableName,
                onValueChange = {},
                label = { Text(text = stringResource(R.string.game_preset)) },
                readOnly = true,
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(
                        expanded = expanded.value
                    )
                },
                modifier = Modifier.fillMaxSize()
            )

            ExposedDropdownMenu(
                expanded = expanded.value,
                onDismissRequest = { expanded.value = false },
            ) {
                AppViewModel.ScoringType.values().forEachIndexed { index, enum ->
                    DropdownMenuItem(
                        onClick = {
                            selectedIndex.value = index
                            expanded.value = !expanded.value
                            Toast.makeText(
                                context,
                                AppViewModel.ScoringType.values()[selectedIndex.value].readableName,
                                Toast.LENGTH_SHORT
                            ).show()
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(enum.readableName)
                    }
                }
            }
        }
    }

    @Composable
    private fun Player(
        name: String,
        context: Context,
        players: MutableList<String>,
        playerName: String
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = MaterialTheme.colors.background, shape = RoundedCornerShape(8))
                .padding(4.dp)
                .clickable {
                    Toast
                        .makeText(context, "Removed '${playerName}'", Toast.LENGTH_SHORT)
                        .show()
                    players.remove(name)
                }
        ) {
            Text(text = name, fontSize = 18.sp, modifier = Modifier.weight(1f, true))
            Icon(Icons.Filled.Remove, "Remove player", tint = MaterialTheme.colors.error)
        }
    }

    @OptIn(ExperimentalFoundationApi::class)
    @Composable
    private fun PlayerInput() {
        val context = LocalContext.current
        val bringIntoViewRequester = remember { BringIntoViewRequester() }
        val coroutineScope = rememberCoroutineScope()

        Card(
            shape = RoundedCornerShape(3.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(PLAYER_LIST_HEIGHT),
            elevation = 4.dp,
            border = BorderStroke(1.dp, MaterialTheme.colors.background),
        ) {
            // PLAYER NAME LIST
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(6.dp),
                contentPadding = PaddingValues(12.dp)

            ) {
                items(players.toList()) { player ->
                    Player(
                        player,
                        context,
                        players,
                        playerName.value
                    )
                }
            }
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Max)
        ) {
            fun addPlayerToGame() {
                if (!isValidLength(playerName.value, 1, 24)) {
                    Toast.makeText(
                        context,
                        "Name must be within valid size.",
                        Toast.LENGTH_SHORT
                    ).show()
                    return
                }

                if (players.contains(playerName.value)) {
                    Toast.makeText(
                        context,
                        "There is already a player with this name.",
                        Toast.LENGTH_SHORT
                    ).show()
                    return
                }

                players.add(playerName.value)
                Toast.makeText(
                    context,
                    "Added player '${playerName.value}'",
                    Toast.LENGTH_SHORT
                ).show()
                playerName.value = ""
            }

            TextField(
                value = playerName.value,
                onValueChange = { playerName.value = it },
                label = { Text(text = stringResource(R.string.player_name_field)) },
                singleLine = true,
                maxLines = 1,
                isError = !isValidLength(playerName.value, 1, 24),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(onDone = {
                    addPlayerToGame()
                }),
                modifier = Modifier
                    .fillMaxSize()
                    .weight(5f, true)
                    .bringIntoViewRequester(bringIntoViewRequester)
                    .onFocusEvent { focusState ->
                        if (focusState.isFocused) {
                            coroutineScope.launch {
                                bringIntoViewRequester.bringIntoView()
                            }
                        }
                    },
            )

            Button(
                onClick = { addPlayerToGame() },
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = Purple500,
                    contentColor = Color.White
                ),
                modifier = Modifier
                    .weight(1f, true)
            ) {
                Icon(
                    Icons.Filled.Add,
                    "Finish creating new game",
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }

    @OptIn(ExperimentalFoundationApi::class)
    @Composable
    fun Modal() {
        val imeState = rememberImeState()

        Scaffold(
            topBar = { TopAppBar() },
            floatingActionButton = {
                if(!imeState.value)
                    FinishGameButton()
            },
            floatingActionButtonPosition = FabPosition.End,
            backgroundColor = MaterialTheme.colors.background
        ) { paddingValues ->
            Box(modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(MaterialTheme.colors.background)) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .padding(20.dp)
                        .background(MaterialTheme.colors.background),
                ) {
                    val bringIntoViewRequester = remember { BringIntoViewRequester() }
                    val coroutineScope = rememberCoroutineScope()

                    TextField(
                        value = gameName.value,
                        onValueChange = { gameName.value = it },
                        label = { Text(text = stringResource(R.string.game_name)) },
                        singleLine = true,
                        isError = !isValidLength(gameName.value, 1, 24),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(INPUT_FIELD_HEIGHT)
                            .bringIntoViewRequester(bringIntoViewRequester)
                            .onFocusEvent { focusState ->
                                if (focusState.isFocused) {
                                    coroutineScope.launch {
                                        bringIntoViewRequester.bringIntoView()
                                    }
                                }
                            },
                    )

                    GameTypeDropdown()

                    // PLAYER INPUT
                    Text(text = "Players", fontSize = 22.sp, fontWeight = FontWeight.Bold)
                    Divider()

                    PlayerInput()
                }
            }
        }
    }
}