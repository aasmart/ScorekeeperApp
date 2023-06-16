package com.example.scorekeeper.game.players

import kotlinx.serialization.Serializable

@Serializable
sealed class AbstractPlayer {
    abstract val name: String
    abstract var score: Int
}


