package com.example.scorekeeper.game.types

import com.example.scorekeeper.game.Player
import com.example.scorekeeper.game.renderers.GameRenderer
import kotlinx.serialization.Polymorphic
import kotlinx.serialization.Serializable

@Serializable
sealed class Game {
    abstract var name: String
    abstract val players: List<Player>
    abstract var playerSortOrder: SortingOrder
    abstract var isComplete: Boolean

    abstract fun getRenderer(): GameRenderer
}

@Serializable
enum class SortingOrder(val readableName: String) {
    ALPHABETICAL(readableName = "Alphabetical Order"),
    REVERSE_ALPHABETICAL(readableName = "Reverse Alphabetical Order")
}
