package com.ddasum.app.data.remote

import com.ddasum.app.data.local.SessionStore
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

/**
 * OkHttp의 인터셉터는 동기 방식이라 토큰을 runBlocking으로 읽는다 — 이 코드는 항상
 * OkHttp 자체의 call-dispatcher 스레드에서 실행되고, 호출자의 코루틴 디스패처나
 * 메인 스레드에서는 절대 실행되지 않기 때문에 안전하다.
 */
class AuthInterceptor @Inject constructor(
    private val sessionStore: SessionStore
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val token = runBlocking { sessionStore.currentAccessToken() }
        val request = chain.request().let { original ->
            if (token.isNullOrBlank()) {
                original
            } else {
                original.newBuilder()
                    .addHeader("Authorization", "Bearer $token")
                    .build()
            }
        }
        return chain.proceed(request)
    }
}
