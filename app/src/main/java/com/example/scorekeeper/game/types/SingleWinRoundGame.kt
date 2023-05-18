package com.example.scorekeeper.game.types

import com.example.scorekeeper.game.Player
import com.example.scorekeeper.game.SortingOrder
import com.example.scorekeeper.game.renderers.SingleWinRoundGameRenderer
import com.example.scorekeeper.game.round.Round
import kotlinx.serialization.Serializable

@Serializable
data class SingleWinRoundGame(
    override var name: String,
    override val players: List<Player>,
    override var playerSortOrder: SortingOrder,
    override var isComplete: Boolean,
    override val rounds: MutableList<Round>,
) : RoundGame() {
    companion object Factory {
        fun new(name: String, playerNames: List<String>): SingleWinRoundGame {
            return SingleWinRoundGame(
                name,
                Player.toPlayerList(playerNames),
                SortingOrder.ALPHABETICAL,
                false,
                mutableListOf()
            )
        }
    }

    override fun getRenderer(): SingleWinRoundGameRenderer {
        return SingleWinRoundGameRenderer(this)
    }

    override fun getCopy(): SingleWinRoundGame {
        return copy(
            name = name,
            players = players,
            playerSortOrder = playerSortOrder,
            isComplete = isComplete,
            rounds = rounds,
        )
    }
}
