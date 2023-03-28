package com.example.cahapp

import androidx.compose.material.AlertDialog
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.ui.platform.LocalConfiguration
import androidx.lifecycle.ViewModel
import com.example.cahapp.game.Game
import com.example.cahapp.game.SingleWinRoundGame
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class AppViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(AppUiState(listOf(), false, null, false))
    val uiState: StateFlow<AppUiState> = _uiState.asStateFlow()

    private val games = mutableStateListOf<Game>()

    fun toggleGameModal() {
        _uiState.value = AppUiState(games, !_uiState.value.isCreatingGame, null, false)
    }

    fun addNewGame(name: String = "New Game", players: List<String>, type: Game.ScoringType) {
        val gameNew = when(type) {
            Game.ScoringType.SIMPLE_SCORING -> Game(name, players)
            Game.ScoringType.ROUNDS_SINGLE -> SingleWinRoundGame(name, players)
        }

        games.add(gameNew)
        _uiState.value = AppUiState(games, false, null, false);
    }

    fun removeGame(game: Game) {
        games.remove(game);
        _uiState.value = AppUiState(games, false, null, false);
    }

    fun getGames(): List<Game> {
        return games.toList();
    }

    fun setFocusedGame(game: Game?) {
        _uiState.value = AppUiState(games, false, game, false);
    }

    fun setFocusedGameVisible(visible: Boolean) {
        _uiState.value = AppUiState(games, false, _uiState.value.focusedGame, visible);
    }
}