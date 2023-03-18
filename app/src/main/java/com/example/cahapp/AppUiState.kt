package com.example.cahapp

import com.example.cahapp.game.Game

data class AppUiState(
    val gameCards: List<Game>,
    val isCreatingGame: Boolean,
    val focusedGame: Game?,
    val isFocusedGameVisible: Boolean
)