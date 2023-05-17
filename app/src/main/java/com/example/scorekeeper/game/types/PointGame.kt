package com.example.scorekeeper.game.types

import com.example.scorekeeper.game.Player
import kotlinx.serialization.Serializable

@Serializable
data class PointGame(
    override var name: String,
    override val players: List<Player>,
    override var playerSortOrder: SortingOrder,
    override var isComplete: Boolean
) : Game() {
    companion object Factory {
        fun new(name: String, playerNames: List<String>): PointGame {
            return PointGame(
                name,
                Player.toPlayerList(playerNames),
                SortingOrder.ALPHABETICAL,
                false
            )
        }
    }
}