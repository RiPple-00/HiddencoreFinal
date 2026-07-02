package com.ddasum.app.data.local

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore by preferencesDataStore(name = "session")

@Singleton
class SessionStore @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private object Keys {
        val ACCESS_TOKEN = stringPreferencesKey("access_token")
        val ROLE = stringPreferencesKey("role")
        val FACILITY_ID = longPreferencesKey("facility_id")
    }

    val accessTokenFlow: Flow<String?> = context.dataStore.data.map { it[Keys.ACCESS_TOKEN] }
    val facilityIdFlow: Flow<Long?> = context.dataStore.data.map { it[Keys.FACILITY_ID] }

    suspend fun currentAccessToken(): String? = accessTokenFlow.first()
    suspend fun currentFacilityId(): Long? = facilityIdFlow.first()

    suspend fun saveSession(accessToken: String, role: String, facilityId: Long?) {
        context.dataStore.edit { prefs ->
            prefs[Keys.ACCESS_TOKEN] = accessToken
            prefs[Keys.ROLE] = role
            if (facilityId != null) {
                prefs[Keys.FACILITY_ID] = facilityId
            } else {
                prefs.remove(Keys.FACILITY_ID)
            }
        }
    }

    suspend fun clear() {
        context.dataStore.edit { it.clear() }
    }
}
