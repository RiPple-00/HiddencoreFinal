package com.ddasum.app.data.repository

import com.ddasum.app.data.model.WeeklyReport
import java.time.LocalDate

interface ReportRepository {
    suspend fun getWeeklyReport(
        patientId: Long,
        startDate: LocalDate? = null,
        endDate: LocalDate? = null,
        language: String = "ko"
    ): WeeklyReport
}
