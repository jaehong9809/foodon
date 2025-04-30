package com.swallaby.foodon.core.data

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class TokenDataStore @Inject constructor(
    @ApplicationContext context: Context
) {
    private val dataStore = context.dataStore

    companion object {
        private val Context.dataStore by preferencesDataStore("token_prefs")
        val ACCESS_TOKEN = stringPreferencesKey("access_token")
        val REFRESH_TOKEN = stringPreferencesKey("refresh_token")
    }

    suspend fun saveTokens(accessToken: String, refreshToken: String) {
        dataStore.edit { prefs ->
            prefs[ACCESS_TOKEN] = accessToken
            prefs[REFRESH_TOKEN] = refreshToken
        }
    }

    val accessTokenFlow: Flow<String?> = dataStore.data
        .map { prefs -> prefs[ACCESS_TOKEN] }

    val refreshTokenFlow: Flow<String?> = dataStore.data
        .map { prefs -> prefs[REFRESH_TOKEN] }

    suspend fun clearTokens() {
        dataStore.edit { it.clear() }
    }
}