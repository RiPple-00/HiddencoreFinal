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

    // 토큰만 암호화한다(TokenCipher) — role/facilityId/userId는 그 자체로는 민감정보가
    // 아니고, DataStore의 edit{} 트랜잭션에서 빠르게 조회·표시하려면 평문이 편함.
    // 유출됐을 때 실제 권한을 주는 값은 토큰 하나뿐이다.
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
