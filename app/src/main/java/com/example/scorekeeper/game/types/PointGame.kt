package com.example.scorekeeper.game.types

import com.example.scorekeeper.game.SortingOrder
import com.example.scorekeeper.game.players.SimplePlayer
import com.example.scorekeeper.game.renderers.PointGameRenderer
import kotlinx.serialization.Serializable

@Serializable
data class PointGame(
    override var name: String,
    override val players: List<SimplePlayer>,
    override var playerSortOrder: SortingOrder,
    override var isComplete: Boolean
) : AbstractGame() {
    companion object Factory {
        fun new(name: String, playerNames: List<String>): PointGame {
            return PointGame(
                name,
                SimplePlayer.fromNames(playerNames),
                SortingOrder.ALPHABETICAL,
                false
            )
        }
    }

    override fun getRenderer(): PointGameRenderer {
        return PointGameRenderer(this)
    }

    override fun getCopy(): PointGame {
        return copy(
            name = name,
            players = players,
            playerSortOrder = playerSortOrder,
            isComplete = isComplete
        )
    }
}