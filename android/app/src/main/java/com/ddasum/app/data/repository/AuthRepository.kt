package com.ddasum.app.data.repository

import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    val isLoggedIn: Flow<Boolean>
    suspend fun guardianLogin(loginId: String, password: String)
    suspend fun logout()
}
