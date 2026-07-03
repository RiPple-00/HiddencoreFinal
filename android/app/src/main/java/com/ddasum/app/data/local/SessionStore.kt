package com.ddasum.app.data.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SessionStore @Inject constructor(
    private val dataStore: DataStore<Preferences>,
    private val tokenCipher: TokenCipher
) {
    private object Keys {
        val ACCESS_TOKEN = stringPreferencesKey("access_token")
        val ROLE = stringPreferencesKey("role")
        val FACILITY_ID = longPreferencesKey("facility_id")
        val USER_ID = longPreferencesKey("user_id")
    }

    // Only the token is encrypted (TokenCipher) — role/facilityId/userId aren't secrets on
    // their own and DataStore's edit{} transaction needs them in plain form to query/display
    // quickly; the token is the one value that grants real access if leaked.
    val accessTokenFlow: Flow<String?> = dataStore.data.map { prefs ->
        prefs[Keys.ACCESS_TOKEN]?.let { tokenCipher.decrypt(it) }
    }
    val facilityIdFlow: Flow<Long?> = dataStore.data.map { it[Keys.FACILITY_ID] }
    val userIdFlow: Flow<Long?> = dataStore.data.map { it[Keys.USER_ID] }

    suspend fun currentAccessToken(): String? = accessTokenFlow.first()
    suspend fun currentFacilityId(): Long? = facilityIdFlow.first()
    suspend fun currentUserId(): Long? = userIdFlow.first()

    suspend fun saveSession(accessToken: String, role: String, facilityId: Long?, userId: Long?) {
        dataStore.edit { prefs ->
            prefs[Keys.ACCESS_TOKEN] = tokenCipher.encrypt(accessToken)
            prefs[Keys.ROLE] = role
            if (facilityId != null) {
                prefs[Keys.FACILITY_ID] = facilityId
            } else {
                prefs.remove(Keys.FACILITY_ID)
            }
            if (userId != null) {
                prefs[Keys.USER_ID] = userId
            } else {
                prefs.remove(Keys.USER_ID)
            }
        }
    }

    suspend fun clear() {
        dataStore.edit { it.clear() }
    }
}
