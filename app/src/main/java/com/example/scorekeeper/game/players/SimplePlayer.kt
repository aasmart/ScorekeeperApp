package com.example.scorekeeper.game.players

import kotlinx.serialization.Serializable

@Serializable
data class SimplePlayer(override val name: String, override var score: Int) : Player() {
    companion object {
        fun fromNames(names: List<String>): List<SimplePlayer> {
            return names.map { SimplePlayer(it, 0) }
        }
    }
}