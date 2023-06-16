package com.example.scorekeeper.game.round

import com.example.scorekeeper.game.players.RoundPlayer
import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
data class Round(
    val placements: List<RoundPlayer>,
    val roundId: String = UUID.randomUUID().toString()
) {
    fun getRenderer(): AbstractRoundRenderer {
        return BasicRoundRenderer(this)
    }
}