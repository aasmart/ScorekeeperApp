package com.example.scorekeeper

import com.example.scorekeeper.game.renderers.AbstractGameRenderer

data class AppUiState(
    val isCreatingGame: Boolean,
    val activeGameRenderer: AbstractGameRenderer?,
    val activeGameAlertDialogVisible: Boolean
)