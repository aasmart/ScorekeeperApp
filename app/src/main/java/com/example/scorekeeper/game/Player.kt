package com.example.scorekeeper.game

import kotlinx.serialization.Serializable

@Serializable
data class Player(var name: String, var score: Int)

fun List<String>.toPlayerList(): List<Player> {
    return this.map { Player(it, 0) }.toList()
}
