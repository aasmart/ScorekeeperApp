package com.example.scorekeeper.game

import kotlinx.serialization.Serializable

@Serializable
data class Player(var name: String, var score: Int) {
    companion object Factory {
        fun toPlayerList(playerNames: List<String>): List<Player> {
            return playerNames.map { Player(it, 0) }.toList()
        }
    }
}


