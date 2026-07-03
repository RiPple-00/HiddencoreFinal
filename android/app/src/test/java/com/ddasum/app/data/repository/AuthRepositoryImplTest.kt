package com.ddasum.app.data.repository

import app.cash.turbine.test
import com.ddasum.app.data.local.SessionStore
import com.ddasum.app.data.remote.api.AuthApiService
import com.ddasum.app.data.remote.dto.auth.GuardianLoginResponse
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class AuthRepositoryImplTest {

    private val api: AuthApiService = mockk()
    // relaxUnitFun이 아니라 relaxed인 이유: AuthRepositoryImpl이 프로퍼티 초기화 시점에
    // sessionStore.accessTokenFlow를 곧바로 읽어버려서, isLoggedIn을 안 쓰는 테스트에서도
    // 기본값이 필요하다.
    private val sessionStore: SessionStore = mockk(relaxed = true)
    private val dispatcher = StandardTestDispatcher()

    private fun repository() = AuthRepositoryImpl(api, sessionStore, dispatcher)

    @Test
    fun `isLoggedIn reflects whether accessTokenFlow currently holds a non-blank token`() = runTest(dispatcher) {
        val tokenFlow = MutableStateFlow<String?>(null)
        every { sessionStore.accessTokenFlow } returns tokenFlow

        repository().isLoggedIn.test {
            assertEquals(false, awaitItem())   // 토큰 없음 → false, 이 레벨에선 null이 절대 안 나옴
            tokenFlow.value = "eyJhbGciOi..."
            assertEquals(true, awaitItem())
            tokenFlow.value = ""
            assertEquals(false, awaitItem())   // 빈 문자열 토큰도 로그아웃 상태로 취급
        }
    }

    @Test
    fun `guardianLogin saves accessToken, role and facilityId from the API response`() = runTest(dispatcher) {
        coEvery { api.guardianLogin(any()) } returns GuardianLoginResponse(
            accessToken = "token-123",
            role = "GUARDIAN",
            facilityId = 7L,
            mustChangePassword = false
        )

        repository().guardianLogin("guardian01", "pw1234")

        // userId는 여기서 검증하지 않는다: JwtDecoder가 android.util.Base64에 의존하는데,
        // 순수 JVM 유닛테스트는 이걸 기본값으로 스텁 처리한다(testOptions.isReturnDefaultValues 참고) —
        // 실제 JWT 디코딩까지 제대로 검증하려면 instrumented test나 Robolectric이 필요하다.
        coVerify {
            sessionStore.saveSession(
                accessToken = "token-123",
                role = "GUARDIAN",
                facilityId = 7L,
                userId = null
            )
        }
    }

    @Test
    fun `logout clears the session store`() = runTest(dispatcher) {
        repository().logout()

        coVerify { sessionStore.clear() }
    }

    @Test
    fun `currentGuardianId returns whatever SessionStore has persisted`() = runTest(dispatcher) {
        coEvery { sessionStore.currentUserId() } returns 42L

        val result = repository().currentGuardianId()

        assertEquals(42L, result)
    }

    @Test
    fun `currentGuardianId returns null when no session has been saved`() = runTest(dispatcher) {
        coEvery { sessionStore.currentUserId() } returns null

        val result = repository().currentGuardianId()

        assertNull(result)
    }
}
