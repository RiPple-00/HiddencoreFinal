package com.ddasum.app.data.remote

import okhttp3.Interceptor
import okhttp3.Response
import java.util.concurrent.TimeUnit
import javax.inject.Inject

/**
 * Some endpoints call out to slow external services inline server-side:
 * weekly-report (LLM, up to ~120s) and medications/scan (HIRA/MFDS lookups, ~65s).
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
