package com.example.scorekeeper.game.round

import com.example.scorekeeper.game.Player
import kotlinx.serialization.Serializable

@Serializable
data class Round(val placements: Map<Player, Int>) {
    fun getRenderer(): RoundRenderer {
        return BasicRoundRenderer(this)
    }
}