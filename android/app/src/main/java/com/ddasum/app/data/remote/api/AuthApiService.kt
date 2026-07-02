package com.ddasum.app.data.remote.api

import com.ddasum.app.data.remote.dto.auth.GuardianLoginRequest
import com.ddasum.app.data.remote.dto.auth.GuardianLoginResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApiService {

    @POST("auth/guardian/login")
    suspend fun guardianLogin(@Body request: GuardianLoginRequest): GuardianLoginResponse
}
