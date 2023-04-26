package com.example.scorekeeper

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.example.scorekeeper.game.types.Game
import com.example.scorekeeper.game.types.RankedRoundGame
import com.example.scorekeeper.game.types.SingleWinRoundGame
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class AppViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(AppUiState(listOf(), false, null))
    val uiState: StateFlow<AppUiState> = _uiState.asStateFlow()

    private val games = mutableStateListOf<Game>()

    enum class ScoringType(val readableName: String, val minPlayers: Int = 1) {
        SIMPLE_SCORING("Simple Scoring"),
        ROUNDS_SINGLE("Round Scoring: Single Winner"),
        RANKED_SCORING("Ranked Round Scoring")
    }

    fun toggleGameModal() {
        _uiState.value = AppUiState(games, !_uiState.value.isCreatingGame, null)
    }

    fun addNewGame(name: String = "New Game", players: List<String>, type: ScoringType) {
        val gameNew = when(type) {
            ScoringType.SIMPLE_SCORING -> Game(name, players)
            ScoringType.ROUNDS_SINGLE -> SingleWinRoundGame(name, players)
            ScoringType.RANKED_SCORING -> RankedRoundGame(name, players)
        }

        games.add(gameNew)
        _uiState.value = AppUiState(games, false, null)
    }

    fun removeGame(game: Game) {
        games.remove(game)
        _uiState.value = AppUiState(games, false, null)
    }

    fun getGames(): List<Game> {
        return games.toList()
    }

    fun setActiveGame(game: Game?) {
        _uiState.value = AppUiState(games, false, game?.copy())
    }

    fun hasFocusedGame(): Boolean {
        return _uiState.value.activeGame != null
    }
}