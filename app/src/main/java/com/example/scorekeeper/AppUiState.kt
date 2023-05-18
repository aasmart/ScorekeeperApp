package com.example.scorekeeper

import com.example.scorekeeper.game.renderers.GameRenderer

data class AppUiState(
    val isCreatingGame: Boolean,
    val activeGameRenderer: GameRenderer?
)