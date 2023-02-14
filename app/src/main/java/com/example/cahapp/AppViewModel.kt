package com.example.cahapp

import androidx.compose.material.AlertDialog
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.example.cahapp.game.Game
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class AppViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(AppUiState(listOf(), false, null))
    val uiState: StateFlow<AppUiState> = _uiState.asStateFlow()

    private val games = mutableStateListOf<Game>()

    fun toggleGameModal() {
        _uiState.value = AppUiState(games, !_uiState.value.isCreatingGame, null)
    }

    fun addNewGame(name: String = "New Game") {
        games.add(Game(name, listOf("Mom", "Dad", "Alex", "Caitlin"), false))
        _uiState.value = AppUiState(games, false, null);
    }

    fun setFocusedGame(game: Game) {
        _uiState.value = AppUiState(games, false, game);
    }
}