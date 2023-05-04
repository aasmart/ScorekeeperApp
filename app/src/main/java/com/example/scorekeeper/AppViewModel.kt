package com.example.scorekeeper

import android.content.Context
import androidx.lifecycle.ViewModel
import com.example.scorekeeper.game.GameStorage
import com.example.scorekeeper.game.types.Game
import com.example.scorekeeper.game.types.RankedRoundGame
import com.example.scorekeeper.game.types.SingleWinRoundGame
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class AppViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(AppUiState(false, null))
    val uiState: StateFlow<AppUiState> = _uiState.asStateFlow()

    enum class ScoringType(val readableName: String, val minPlayers: Int = 1) {
        SIMPLE_SCORING("Simple Scoring"),
        ROUNDS_SINGLE("Round Scoring: Single Winner"),
        RANKED_SCORING("Ranked Round Scoring")
    }

    fun toggleGameModal() {
        _uiState.value = AppUiState(!_uiState.value.isCreatingGame, null)
    }

    suspend fun addNewGame(context: Context, name: String = "New Game", players: List<String>, type: ScoringType) {
        val gameNew = when(type) {
            ScoringType.SIMPLE_SCORING -> Game(name)
            ScoringType.ROUNDS_SINGLE -> SingleWinRoundGame(name)
            ScoringType.RANKED_SCORING -> RankedRoundGame(name)
        }

        gameNew.setPlayers(players)

        _uiState.value = AppUiState(false, null)
        GameStorage.getInstance(context).addGame(gameNew)
    }

    suspend fun removeGame(context: Context, game: Game) {
        _uiState.value = AppUiState(false, null)
        GameStorage.getInstance(context).removeGame(game)
    }

    suspend fun setActiveGame(context: Context, game: Game?) {
        _uiState.value = AppUiState(false, game?.copy())
        /*if (game != null) {
            GameStorage.getInstance(context).addGame(game)
        }*/
    }

    fun hasFocusedGame(): Boolean {
        return _uiState.value.activeGame != null
    }
}