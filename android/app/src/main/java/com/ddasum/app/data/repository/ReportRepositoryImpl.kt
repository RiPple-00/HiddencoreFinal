package com.ddasum.app.data.repository

import com.ddasum.app.data.model.WeeklyReport
import com.ddasum.app.data.remote.api.ReportApiService
import com.ddasum.app.data.remote.dto.report.toDomain
import java.time.LocalDate
import javax.inject.Inject

class ReportRepositoryImpl @Inject constructor(
    private val api: ReportApiService
) : ReportRepository {

    override suspend fun getWeeklyReport(
        patientId: Long,
        startDate: LocalDate?,
        endDate: LocalDate?,
        language: String
    ): WeeklyReport =
        api.getWeeklyReport(patientId, startDate?.toString(), endDate?.toString(), language).toDomain()
}
