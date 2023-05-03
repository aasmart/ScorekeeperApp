package com.example.scorekeeper

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.example.scorekeeper.game.GameStorage
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

    suspend fun addNewGame(gameStorage: GameStorage, name: String = "New Game", players: List<String>, type: ScoringType) {
        val gameNew = when(type) {
            ScoringType.SIMPLE_SCORING -> Game(name)
            ScoringType.ROUNDS_SINGLE -> SingleWinRoundGame(name)
            ScoringType.RANKED_SCORING -> RankedRoundGame(name)
        }

        gameNew.setPlayers(players)

        games.add(gameNew)
        gameStorage.saveGames(games)
        _uiState.value = AppUiState(games, false, null)
    }

    suspend fun removeGame(gameStorage: GameStorage, game: Game) {
        games.remove(game)
        gameStorage.saveGames(games)
        _uiState.value = AppUiState(games, false, null)
    }

    fun getGames(): List<Game> {
        return games.toList()
    }

    suspend fun setActiveGame(gameStorage: GameStorage, game: Game?) {
        gameStorage.saveGames(games)
        _uiState.value = AppUiState(games, false, game?.copy())
    }

    fun hasFocusedGame(): Boolean {
        return _uiState.value.activeGame != null
    }
}