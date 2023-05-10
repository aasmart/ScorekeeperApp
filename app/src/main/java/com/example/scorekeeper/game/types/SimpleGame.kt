package com.example.scorekeeper.game.types

import com.example.scorekeeper.game.Player
import kotlinx.serialization.Serializable

@Serializable
data class SimpleGame(
    override var name: String,
    override val players: List<Player>,
    override var playerSortOrder: SortingOrder,
    override var isComplete: Boolean
) : Game()