package com.example.scorekeeper

import android.content.Context
import androidx.lifecycle.ViewModel
import com.example.scorekeeper.game.GameStorage
import com.example.scorekeeper.game.types.AbstractGame
import com.example.scorekeeper.game.types.PointGame
import com.example.scorekeeper.game.types.RankedRoundGame
import com.example.scorekeeper.game.types.SingleWinRoundGame
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class AppViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(AppUiState(false, null, false))
    val uiState: StateFlow<AppUiState> = _uiState.asStateFlow()

    enum class ScoringType(val readableName: String, val minPlayers: Int = 1) {
        SIMPLE_SCORING("Simple Scoring"),
        ROUNDS_SINGLE("Round Scoring: Single Winner"),
        RANKED_SCORING("Ranked Round Scoring")
    }

    fun toggleGameModal() {
        _uiState.value = AppUiState(!_uiState.value.isCreatingGame, null, false)
    }

    suspend fun addNewGame(context: Context, name: String = "New Game", players: List<String>, type: ScoringType) {
        val gameNew = when(type) {
            ScoringType.SIMPLE_SCORING -> PointGame.new(name, players)
            ScoringType.ROUNDS_SINGLE -> SingleWinRoundGame.new(name, players)
            ScoringType.RANKED_SCORING -> RankedRoundGame.new(name, players)
        }

        _uiState.value = AppUiState(false, null, false)
        GameStorage.getInstance(context).addGame(gameNew)
    }

    suspend fun removeGame(context: Context, game: AbstractGame) {
        _uiState.value = AppUiState(false, null, false)
        GameStorage.getInstance(context).removeGame(game)
    }

    suspend fun setActiveGame(context: Context, game: AbstractGame?) {
        _uiState.value = AppUiState(false, game?.getRenderer(), false)
        if (game != null)
            GameStorage.getInstance(context).setGame(game)
    }

    fun hasFocusedGame(): Boolean {
        return _uiState.value.activeGameRenderer != null
    }

    fun setFinishGameAlertDialogVisible(visible: Boolean) {
        _uiState.value = AppUiState(false, _uiState.value.activeGameRenderer, visible)
    }
}