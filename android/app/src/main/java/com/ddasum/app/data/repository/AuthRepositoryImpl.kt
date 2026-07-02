package com.ddasum.app.data.repository

import com.ddasum.app.data.local.SessionStore
import com.ddasum.app.data.remote.api.AuthApiService
import com.ddasum.app.data.remote.dto.auth.GuardianLoginRequest
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val api: AuthApiService,
    private val sessionStore: SessionStore
) : AuthRepository {

    override val isLoggedIn: Flow<Boolean> =
        sessionStore.accessTokenFlow.map { !it.isNullOrBlank() }

    override suspend fun guardianLogin(loginId: String, password: String) {
        val response = api.guardianLogin(GuardianLoginRequest(loginId, password))
        sessionStore.saveSession(
            accessToken = response.accessToken,
            role = response.role,
            facilityId = response.facilityId
        )
    }

    override suspend fun logout() {
        sessionStore.clear()
    }
}
