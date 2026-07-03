package com.ddasum.app.data.repository

import com.ddasum.app.data.model.WeeklyReport
import com.ddasum.app.data.remote.api.ReportApiService
import com.ddasum.app.data.remote.dto.report.toDomain
import com.ddasum.app.di.IoDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import java.time.LocalDate
import javax.inject.Inject

class ReportRepositoryImpl @Inject constructor(
    private val api: ReportApiService,
    @param:IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : ReportRepository {

    override suspend fun getWeeklyReport(
        patientId: Long,
        startDate: LocalDate?,
        endDate: LocalDate?,
        language: String
    ): WeeklyReport =
        withContext(ioDispatcher) {
            api.getWeeklyReport(patientId, startDate?.toString(), endDate?.toString(), language).toDomain()
        }
}
