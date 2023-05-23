package com.example.scorekeeper.game.round

import com.example.scorekeeper.game.players.RoundPlayer
import kotlinx.serialization.Serializable

@Serializable
data class Round(val placements: List<RoundPlayer>) {
    fun getRenderer(): RoundRenderer {
        return BasicRoundRenderer(this)
    }
}