package com.example.scorekeeper

import com.example.scorekeeper.game.types.Game

data class AppUiState(
    val isCreatingGame: Boolean,
    val activeGame: Game?,
)