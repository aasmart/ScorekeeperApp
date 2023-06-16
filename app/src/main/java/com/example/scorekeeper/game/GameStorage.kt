package com.example.scorekeeper.game

import android.annotation.SuppressLint
import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.scorekeeper.game.types.AbstractGame
import com.example.scorekeeper.game.types.RankedRoundGame
import com.example.scorekeeper.game.types.SingleWinRoundGame
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic

class GameStorage(private val context: Context) {
    companion object {
        @SuppressLint("StaticFieldLeak")
        private var Instance: GameStorage? = null

        private val Context.dataStore by preferencesDataStore(name = "game_saves")
        val GAME_SAVES = stringPreferencesKey("game_saves")

        @Synchronized
        fun getInstance(context: Context): GameStorage {
            return Instance ?: run {
                val instance = GameStorage(context.applicationContext)
                Instance = instance
                instance
            }
        }

        private val module = SerializersModule {
            polymorphic(AbstractGame::class) {
                subclass(SingleWinRoundGame::class, SingleWinRoundGame.serializer())
                subclass(RankedRoundGame::class, RankedRoundGame.serializer())
            }
        }

        private val gameJson = Json {
            serializersModule = module
            allowStructuredMapKeys = true
        }
    }

    fun getGames(): Flow<List<AbstractGame>> {
        return context.dataStore.data
            .map { preferences ->
                preferences[GAME_SAVES]?.let {json ->
                    gameJson.decodeFromString(json)
                } ?: emptyList()
            }
    }

    suspend fun addGame(game: AbstractGame) {
        context.dataStore.edit { preferences ->
            val games = preferences[GAME_SAVES]?.let { json ->
                gameJson.decodeFromString<List<AbstractGame>>(json)
            }?.toMutableList() ?: mutableListOf()

            games.add(game)
            preferences[GAME_SAVES] = gameJson.encodeToString(games)
        }
    }

    suspend fun removeGame(game: AbstractGame) {
        context.dataStore.edit { preferences ->
            val games = preferences[GAME_SAVES]?.let { json ->
                gameJson.decodeFromString<List<AbstractGame>>(json)
            }?.toMutableList() ?: return@edit

            games.removeAll { g -> g.gameId == game.gameId }
            preferences[GAME_SAVES] = gameJson.encodeToString(games)
        }
    }

    suspend fun setGame(game: AbstractGame) {
        context.dataStore.edit { preferences ->
            val games = preferences[GAME_SAVES]?.let { json ->
                gameJson.decodeFromString<List<AbstractGame>>(json)
            }?.toMutableList() ?: return@edit

            games.replaceAll { g -> if(g.gameId == game.gameId) game else g }
            preferences[GAME_SAVES] = gameJson.encodeToString(games)
        }
    }

    suspend fun clearGame() {
        context.dataStore.edit { it.remove(GAME_SAVES) }
    }
}