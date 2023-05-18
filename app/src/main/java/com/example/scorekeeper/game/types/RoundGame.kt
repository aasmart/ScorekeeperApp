package com.example.scorekeeper.game.types

import com.example.scorekeeper.game.round.Round
import kotlinx.serialization.Polymorphic
import kotlinx.serialization.Serializable

@Polymorphic
@Serializable
sealed class RoundGame : Game() {
    abstract val rounds: MutableList<Round>
}