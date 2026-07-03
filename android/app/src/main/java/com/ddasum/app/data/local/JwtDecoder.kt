package com.ddasum.app.data.local

import android.util.Base64
import com.google.gson.Gson

/**
 * JWT payload에서 클레임만 꺼내 읽는다 — 서명 검증은 하지 않는다. 이 값은 QR 스캔의
 * guardianId처럼 요청 body를 만드는 데만 쓰이고, 서버가 모든 요청마다 토큰 서명을
 * 독립적으로 재검증하기 때문에 로컬에서 값이 변조돼도 실제 권한은 얻을 수 없어 안전하다.
 */
object JwtDecoder {
    private val gson = Gson()

    private data class Claims(val sub: String? = null)

    /** JwtService.createAccessToken()이 `subject = userId.toString()`로 넣은 값을 반대로 꺼낸다. */
    fun decodeUserId(token: String): Long? = runCatching {
        val payload = token.split(".")[1]
        val json = String(Base64.decode(payload, Base64.URL_SAFE or Base64.NO_WRAP or Base64.NO_PADDING))
        gson.fromJson(json, Claims::class.java).sub?.toLongOrNull()
    }.getOrNull()
}
