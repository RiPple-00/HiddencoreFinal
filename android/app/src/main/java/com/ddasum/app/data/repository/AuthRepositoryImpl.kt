package com.ddasum.app.data.repository

import com.ddasum.app.data.local.JwtDecoder
import com.ddasum.app.data.local.SessionStore
import com.ddasum.app.data.remote.api.AuthApiService
import com.ddasum.app.data.remote.dto.auth.GuardianLoginRequest
import com.ddasum.app.di.IoDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val api: AuthApiService,
    private val sessionStore: SessionStore,
    @param:IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : AuthRepository {

    // DataStore's own Flow already dispatches off the caller's thread — nothing to wrap here.
    // 이 레벨에서는 null이 나올 수 없다 — 토큰이 없으면(로그인 기록 없음) false, 있으면 true로 항상 확정된다.
    // accessTokenFlow: Flow<String?> (토큰없음=null) → isNullOrBlank()로 뒤집어서 Boolean(non-null)으로 변환.
    // (AuthViewModel이 이걸 stateIn으로 감싸면서 "아직 응답 전"을 뜻하는 null을 그 계층에서만 별도로 추가함)
    override val isLoggedIn: Flow<Boolean> =
        sessionStore.accessTokenFlow.map { !it.isNullOrBlank() }

    override suspend fun guardianLogin(loginId: String, password: String) {
        withContext(ioDispatcher) {
            val response = api.guardianLogin(GuardianLoginRequest(loginId, password))
            // 로그인 응답 자체엔 guardianId가 없어서, 토큰의 sub 클레임을 읽어서 보충한다.
            val userId = JwtDecoder.decodeUserId(response.accessToken)
            sessionStore.saveSession(
                accessToken = response.accessToken,
                role = response.role,
                facilityId = response.facilityId,
                userId = userId
            )
        }
    }

    override suspend fun logout() {
        withContext(ioDispatcher) {
            sessionStore.clear()
        }
    }

    override suspend fun currentGuardianId(): Long? =
        withContext(ioDispatcher) { sessionStore.currentUserId() }
}
