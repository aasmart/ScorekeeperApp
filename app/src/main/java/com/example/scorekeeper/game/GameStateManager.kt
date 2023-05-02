package com.example.scorekeeper.game

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

class GameStateManager(private val context: Context) {
    private val Context.dataStore by preferencesDataStore(name = "game_saves")

    companion object {
        val GAME_SAVES = stringPreferencesKey("game_saves")
    }

    suspend fun saveGames(games: List<Game>) {
        context.dataStore.edit { gameSaves ->
            gameSaves[GAME_SAVES] = Json.encodeToString(games)
        }
    }

    fun loadGames(): Flow<List<Game>> {
        return context.dataStore.data
            .map { preferences ->
                preferences[GAME_SAVES]?.let {json ->
                    Json.decodeFromString(json)
                } ?: emptyList()
            }
    }
}