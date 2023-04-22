package com.example.scorekeeper.game

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.example.scorekeeper.dataStore
import com.example.scorekeeper.game.types.Game
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GameManager(private val context: Context) {
    private val dataStore = context.dataStore;
    private val GAME_SAVES = stringPreferencesKey("game_saves")

    suspend fun saveGames(games: List<Game>) {
        context.dataStore.edit { gameSaves ->
            gameSaves[GAME_SAVES] = ""
        }
    }

    fun loadGames(): List<Game> {
        val gameJson = context.dataStore.data
            .map { preferences ->
                preferences[GAME_SAVES] ?: ""
        }

        return listOf();
    }
}