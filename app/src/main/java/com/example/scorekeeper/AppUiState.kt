package com.example.scorekeeper

import com.example.scorekeeper.game.types.Game

data class AppUiState(
    val gameCards: List<Game>,
    val isCreatingGame: Boolean,
    val focusedGame: Game?,
    val isFocusedGameVisible: Boolean
)