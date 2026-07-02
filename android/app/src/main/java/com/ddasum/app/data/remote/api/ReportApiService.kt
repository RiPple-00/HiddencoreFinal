package com.ddasum.app.data.remote.api

import com.ddasum.app.data.remote.dto.report.WeeklyReportResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ReportApiService {

    /**
     * Backend generates this synchronously (calls an LLM service inline) and can
     * take up to ~120s — see LongTimeoutInterceptor in NetworkModule.
     */
    @GET("guardian/me/patients/{patientId}/weekly-report")
    suspend fun getWeeklyReport(
        @Path("patientId") patientId: Long,
        @Query("startDate") startDate: String? = null,
        @Query("endDate") endDate: String? = null,
        @Query("language") language: String = "ko"
    ): WeeklyReportResponse
}
