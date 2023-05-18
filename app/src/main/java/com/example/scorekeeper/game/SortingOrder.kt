package com.example.scorekeeper.game

import kotlinx.serialization.Serializable

@Serializable
enum class SortingOrder(val readableName: String) {
    ALPHABETICAL(readableName = "Alphabetical Order"),
    REVERSE_ALPHABETICAL(readableName = "Reverse Alphabetical Order")
}
