package com.ddasum.app.data.remote

import okhttp3.Interceptor
import okhttp3.Response
import java.util.concurrent.TimeUnit
import javax.inject.Inject

/**
 * 일부 엔드포인트는 서버 내부에서 느린 외부 서비스를 동기적으로 호출한다:
 * weekly-report(LLM 호출, 최대 ~120초), medications/scan(HIRA/MFDS 조회, ~65초).
 */
class LongTimeoutInterceptor @Inject constructor() : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val path = chain.request().url.encodedPath
        val timeoutSeconds = when {
            path.contains("weekly-report") -> 120L
            path.contains("medications/scan") -> 65L
            else -> null
        }
        val request = chain.request()
        return if (timeoutSeconds != null) {
            chain.withReadTimeout(timeoutSeconds.toInt(), TimeUnit.SECONDS)
                .withConnectTimeout(timeoutSeconds.toInt(), TimeUnit.SECONDS)
                .proceed(request)
        } else {
            chain.proceed(request)
        }
    }
}
