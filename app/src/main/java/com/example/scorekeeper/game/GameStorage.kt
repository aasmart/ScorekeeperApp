package com.example.scorekeeper.game

import android.annotation.SuppressLint
import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.scorekeeper.game.types.Game
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

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
    }

    fun getGames(): Flow<List<Game>> {
        return context.dataStore.data
            .map { preferences ->
                preferences[GAME_SAVES]?.let {json ->
                    Json.decodeFromString(json)
                } ?: emptyList()
            }
    }

    suspend fun addGame(game: Game) {
        context.dataStore.edit { preferences ->
            val games = preferences[GAME_SAVES]?.let { json ->
                Json.decodeFromString<List<Game>>(json)
            }?.toMutableList() ?: mutableListOf()

            games.add(game)
            preferences[GAME_SAVES] = Json.encodeToString(games)
        }
    }

    suspend fun removeGame(game: Game) {
        context.dataStore.edit { preferences ->
            val games = preferences[GAME_SAVES]?.let { json ->
                Json.decodeFromString<List<Game>>(json)
            }?.toMutableList() ?: return@edit

            games.removeAll { g -> g.name == game.name }
            preferences[GAME_SAVES] = Json.encodeToString(games)
        }
    }

    suspend fun setGame(game: Game) {
        context.dataStore.edit { preferences ->
            val games = preferences[GAME_SAVES]?.let { json ->
                Json.decodeFromString<List<Game>>(json)
            }?.toMutableList() ?: return@edit

            games.replaceAll { g -> if(g.name == game.name) game else g }
            preferences[GAME_SAVES] = Json.encodeToString(games)
        }
    }

    suspend fun clearGame() {
        context.dataStore.edit { it.remove(GAME_SAVES) }
    }
}