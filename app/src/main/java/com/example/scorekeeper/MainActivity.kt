package com.example.scorekeeper

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.node.Ref
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.scorekeeper.game.types.Game
import com.example.scorekeeper.ui.theme.CAHAppTheme
import com.example.scorekeeper.ui.theme.Purple500
import com.example.scorekeeper.ui.theme.Purple700

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "games")

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.decorView.setBackgroundResource(R.color.black)

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
                    TitleText(stringResource(R.string.app_name))
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
        isFloatingActionButtonDocked = true,
        backgroundColor = MaterialTheme.colors.background
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

    AnimatedVisibility(
        visible = appUiState.isCreatingGame,
        enter = slideInHorizontally() {
                maxWidth -> maxWidth / 3
        } + fadeIn(
            // Fade in with the initial alpha of 0.3f.
            initialAlpha = 0.3f
        ),
        exit = slideOutHorizontally() { maxWidth -> maxWidth / 3 } + fadeOut()
    ) {
        BackHandler(enabled = true) {
            appViewModel.toggleGameModal()
        }
        CreateGameForm(appViewModel).Modal()
    }

    AnimatedVisibility(
        visible = appViewModel.hasFocusedGame(),
        enter = slideInHorizontally() {
                maxWidth -> maxWidth / 3
        } + fadeIn(
            // Fade in with the initial alpha of 0.3f.
            initialAlpha = 0.3f
        ),
        exit = slideOutHorizontally() { maxWidth -> maxWidth / 3 } + fadeOut()
    ) {
        BackHandler(enabled = true) {
            //appViewModel.setActiveGame(null)
        }

        val ref = remember { Ref<Game>() }

        ref.value = appUiState.activeGame ?: ref.value
        ref.value?.GamePage(appViewModel)

        Log.wtf("Huh", "Rerendered")
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

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {

}