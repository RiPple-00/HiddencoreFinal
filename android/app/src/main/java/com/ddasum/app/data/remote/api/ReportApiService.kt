package com.ddasum.app.data.remote.api

import com.ddasum.app.data.remote.dto.report.WeeklyReportResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ReportApiService {

    /**
     * 백엔드가 이 응답을 동기적으로 생성한다(내부에서 LLM 서비스를 직접 호출) — 최대 ~120초까지
     * 걸릴 수 있다. NetworkModule의 LongTimeoutInterceptor 참고.
     */
    @GET("guardian/me/patients/{patientId}/weekly-report")
    suspend fun getWeeklyReport(
        @Path("patientId") patientId: Long,
        @Query("startDate") startDate: String? = null,
        @Query("endDate") endDate: String? = null,
        @Query("language") language: String = "ko"
    ): WeeklyReportResponse
}
