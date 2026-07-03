package com.ddasum.app.data.local

import android.util.Base64
import com.google.gson.Gson

/**
 * Reads claims out of a JWT payload without verifying the signature — safe here
 * because this value is only used to build outgoing request bodies (e.g. QR scan's
 * guardianId); the backend independently re-validates the token's signature on
 * every request, so a tampered local copy can't grant any real access.
 */
object JwtDecoder {
    private val gson = Gson()

    private data class Claims(val sub: String? = null)

    /** JwtService.createAccessToken() sets `subject = userId.toString()` — this reverses that. */
    fun decodeUserId(token: String): Long? = runCatching {
        val payload = token.split(".")[1]
        val json = String(Base64.decode(payload, Base64.URL_SAFE or Base64.NO_WRAP or Base64.NO_PADDING))
        gson.fromJson(json, Claims::class.java).sub?.toLongOrNull()
    }.getOrNull()
}
