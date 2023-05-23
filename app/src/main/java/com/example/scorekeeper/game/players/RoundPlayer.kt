package com.example.scorekeeper.game.players

import kotlinx.serialization.Serializable

@Serializable
data class RoundPlayer(
    override val name: String,
    override var score: Int,
    var rank: Int
) : Player() {
    companion object {
        fun fromNames(names: List<String>): List<RoundPlayer> {
            return names.map { RoundPlayer(it, 0, -1) }
        }
    }
}