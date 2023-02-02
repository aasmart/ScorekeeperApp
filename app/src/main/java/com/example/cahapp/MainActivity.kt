package com.example.cahapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.cahapp.game.Game
import com.example.cahapp.ui.theme.CAHAppTheme
import com.example.cahapp.ui.theme.Purple500
import com.example.cahapp.ui.theme.Purple700
import org.w3c.dom.Text

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

    Scaffold(
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
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize()
        ) {
            TitleText("Cards Against Humanity")

            if(appUiState.isCreatingGame) {
                NewGameModal(appViewModel)
            }

            GameList(appUiState.gameCards)
        }
    }
}

@Composable
fun TitleText(title: String) {
    Text(
        text = title,
        fontWeight = FontWeight.Bold,
        fontSize = 30.sp,
        modifier = Modifier.padding(12.dp),
        color = Color.White,
    )
}

@Composable
fun ColumnScope.GameList(games: List<Game>) {
    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .fillMaxWidth()
            .weight(1f, true)
            .background(MaterialTheme.colors.background),
        horizontalAlignment = Alignment.CenterHorizontally

    ) {
        games.forEach { game -> game.GetAsCard() }
    }
}

@Composable
fun NewGameButton(addGame : () -> Unit) {
    FloatingActionButton(
        onClick = addGame,
        backgroundColor = Purple500,
    ) {
        Icon(Icons.Filled.Add, "Game")
    }
}

@Composable
fun NewGameModal(appViewModel: AppViewModel) {
    // The name for the new game being created
    val name = remember { mutableStateOf("") }

    fun isValidName(): Boolean {
        return name.value.length in 1..16
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Card(
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier.padding(10.dp,5.dp,10.dp,10.dp),
                elevation = 8.dp,
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(16.dp),
                ) {
                    Text(
                        text = "Create New Game",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Black
                    )

                    Column() {
                        OutlinedTextField(
                            value = name.value,
                            onValueChange = { name.value = it },
                            label = { Text(text = stringResource(R.string.game_name)) },
                            singleLine = true,
                            isError = !isValidName()
                        )
                    }

                    Row(
                        modifier = Modifier.padding(all = 8.dp),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Button(
                            onClick = { appViewModel.toggleGameModal() },
                            modifier = Modifier.padding(end = 16.dp)
                        ) {
                            Text(text = stringResource(R.string.cancel))
                        }

                        Button(
                            onClick = { appViewModel.addNewGame(name.value) }, enabled = isValidName()
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