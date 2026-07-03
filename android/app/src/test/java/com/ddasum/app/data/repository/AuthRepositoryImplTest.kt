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
    // relaxed(not just relaxUnitFun): AuthRepositoryImpl reads sessionStore.accessTokenFlow
    // eagerly in its property initializer, so every test needs a default even when it
    // doesn't care about isLoggedIn specifically.
    private val sessionStore: SessionStore = mockk(relaxed = true)
    private val dispatcher = StandardTestDispatcher()

    private fun repository() = AuthRepositoryImpl(api, sessionStore, dispatcher)

    @Test
    fun `isLoggedIn reflects whether accessTokenFlow currently holds a non-blank token`() = runTest(dispatcher) {
        val tokenFlow = MutableStateFlow<String?>(null)
        every { sessionStore.accessTokenFlow } returns tokenFlow

        repository().isLoggedIn.test {
            assertEquals(false, awaitItem())   // no token yet → false, never null at this layer
            tokenFlow.value = "eyJhbGciOi..."
            assertEquals(true, awaitItem())
            tokenFlow.value = ""
            assertEquals(false, awaitItem())   // blank token also counts as logged out
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

        // userId isn't asserted here: JwtDecoder depends on android.util.Base64, which plain
        // JVM unit tests stub to return default values (see testOptions.isReturnDefaultValues) —
        // properly verifying real JWT decoding needs an instrumented test or Robolectric.
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
