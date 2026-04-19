package com.ironlog.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "ironlog_prefs")

class SessionRepository(private val context: Context) {
    private val SESSIONS_KEY = stringPreferencesKey("ironlog:sessions")

    val sessionsFlow: Flow<List<Session>> = context.dataStore.data.map { preferences ->
        val sessionsJson = preferences[SESSIONS_KEY] ?: "[]"
        try {
            Json.decodeFromString<List<Session>>(sessionsJson)
        } catch (e: Exception) {
            emptyList()
        }
    }

    suspend fun saveSessions(sessions: List<Session>) {
        context.dataStore.edit { preferences ->
            preferences[SESSIONS_KEY] = Json.encodeToString(sessions)
        }
    }
}
