package com.example.scorekeeper.game.types
import com.example.scorekeeper.R
import org.json.JSONObject

open class Game(var name: String, players: List<String>) {
    val playerScores = HashMap<String, Int>()
    var playerSortOrder = SortingOrder.ALPHABETICAL
    var isComplete = false
    companion object {
        val podiumPlaces =  arrayOf(PodiumPlace.SECOND, PodiumPlace.FIRST, PodiumPlace.THIRD)
    }

    init {
        players.forEach { name ->
            playerScores[name] = 0
        }
    }

    enum class PodiumPlace(val rankingInt: Int, val colorId: Int) {
        FIRST(1, R.color.gold),
        SECOND(2, R.color.silver),
        THIRD(3, R.color.bronze)
    }

    enum class SortingOrder(val readableName: String) {
        ALPHABETICAL(readableName = "Alphabetical Order"),
        REVERSE_ALPHABETICAL(readableName = "Reverse Alphabetical Order")
    }

    fun getPlayerNamesSorted(): Map<String, Int> {
        return when (playerSortOrder) {
            SortingOrder.ALPHABETICAL -> playerScores.toList().sortedBy { (name, _) -> name }.toMap()
            SortingOrder.REVERSE_ALPHABETICAL -> playerScores.toList().sortedByDescending { (name, _) -> name }.toMap()
        }
    }

    fun updateScore(playerName: String, amount: Int = 1) {
        playerScores.replace(playerName, playerScores.getValue(playerName) + amount)
    }

    fun toJson(): JSONObject {
        val json = JSONObject()
        json.put("name", name)

        return json
    }
}