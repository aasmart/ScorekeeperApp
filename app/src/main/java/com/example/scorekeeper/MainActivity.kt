package com.example.scorekeeper

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.BottomAppBar
import androidx.compose.material.FabPosition
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
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
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.scorekeeper.game.GameStorage
import com.example.scorekeeper.game.types.Game
import com.example.scorekeeper.ui.theme.CAHAppTheme
import com.example.scorekeeper.ui.theme.Purple500
import com.example.scorekeeper.ui.theme.Purple700
import kotlinx.coroutines.launch

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
    val gameStorage = GameStorage(context)
    val scope = rememberCoroutineScope()

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
                GameList(gameStorage, appViewModel)
            }
        }
    }

    AnimatedVisibility(
        visible = appUiState.isCreatingGame,
        enter = slideInHorizontally {
                maxWidth -> maxWidth / 3
        } + fadeIn(
            // Fade in with the initial alpha of 0.3f.
            initialAlpha = 0.3f
        ),
        exit = slideOutHorizontally { maxWidth -> maxWidth / 3 } + fadeOut()
    ) {
        BackHandler(enabled = true) {
            appViewModel.toggleGameModal()
        }
        CreateGameForm(appViewModel).Modal(gameStorage)
    }

    AnimatedVisibility(
        visible = appViewModel.hasFocusedGame(),
        enter = slideInHorizontally {
                maxWidth -> maxWidth / 3
        } + fadeIn(
            // Fade in with the initial alpha of 0.3f.
            initialAlpha = 0.3f
        ),
        exit = slideOutHorizontally { maxWidth -> maxWidth / 3 } + fadeOut()
    ) {
        BackHandler(enabled = true) {
            scope.launch {
                appViewModel.setActiveGame(gameStorage, null)
            }
        }

        val ref = remember { Ref<Game>() }

        ref.value = appUiState.activeGame ?: ref.value
        ref.value?.GamePage(appViewModel, gameStorage)
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
fun ColumnScope.GameList(gameStorage: GameStorage, appViewModel: AppViewModel) {
    val games: List<Game> = gameStorage.loadGames().collectAsState(initial = emptyList()).value

    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .weight(1f, true)
            .background(MaterialTheme.colors.background),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp),
        contentPadding = PaddingValues(12.dp)

    ) {
        items(games) { game -> game.GetAsCard(appViewModel, gameStorage) }
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