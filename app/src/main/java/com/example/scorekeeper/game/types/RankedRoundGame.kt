package com.example.scorekeeper.game.types

import com.example.scorekeeper.game.SortingOrder
import com.example.scorekeeper.game.players.RoundPlayer
import com.example.scorekeeper.game.renderers.RankedRoundGameRenderer
import com.example.scorekeeper.game.round.Round
import kotlinx.serialization.Serializable

@Serializable
data class RankedRoundGame(
    override var name: String,
    override val players: List<RoundPlayer>,
    override var playerSortOrder: SortingOrder,
    override var isComplete: Boolean,
    override val rounds: MutableList<Round>,
    val placementNumbers: List<Int>
) : RoundGame() {
    companion object Factory {
        fun new(name: String, playerNames: List<String>): RankedRoundGame {
            val players = RoundPlayer.fromNames(playerNames)
            return RankedRoundGame(
                name,
                players,
                SortingOrder.ALPHABETICAL,
                false,
                mutableListOf(),
                (1..players.size).toList()
            )
        }
    }

    override fun getRenderer(): RankedRoundGameRenderer {
        return RankedRoundGameRenderer(this)
    }

    override fun getCopy(): RankedRoundGame {
        return copy(
            name = name,
            players = players,
            playerSortOrder = playerSortOrder,
            isComplete = isComplete,
            rounds = rounds,
            placementNumbers = placementNumbers
        )
    }
}
