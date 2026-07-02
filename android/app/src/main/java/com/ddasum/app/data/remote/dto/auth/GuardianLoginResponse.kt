package com.ddasum.app.data.remote.dto.auth

/** Mirrors backend GuardianLoginResponse (POST /api/auth/guardian/login). */
data class GuardianLoginResponse(
    val accessToken: String,
    val role: String,
    val facilityId: Long?,
    val mustChangePassword: Boolean
)
