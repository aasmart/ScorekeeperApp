package com.example.scorekeeper.game.types

import com.example.scorekeeper.game.players.AbstractPlayer
import com.example.scorekeeper.game.SortingOrder
import com.example.scorekeeper.game.renderers.AbstractGameRenderer
import kotlinx.serialization.Serializable

@Serializable
sealed class AbstractGame {
    abstract var name: String
    abstract val players: List<AbstractPlayer>
    abstract var playerSortOrder: SortingOrder
    abstract var isComplete: Boolean

    abstract fun getRenderer(): AbstractGameRenderer
    abstract fun getCopy(): AbstractGame
}