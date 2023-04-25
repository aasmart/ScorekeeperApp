package com.example.scorekeeper

import com.example.scorekeeper.game.renderers.GameRenderer
import com.example.scorekeeper.game.types.Game

data class AppUiState(
    val gameCards: List<Game>,
    val isCreatingGame: Boolean,
    val gameRenderer: GameRenderer<Game>?
)