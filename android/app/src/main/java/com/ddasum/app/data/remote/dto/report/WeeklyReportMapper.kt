package com.ddasum.app.data.remote.dto.report

import com.ddasum.app.data.model.ChecklistRow
import com.ddasum.app.data.model.DailyRate
import com.ddasum.app.data.model.DiagnosisSection
import com.ddasum.app.data.model.MealMissingCount
import com.ddasum.app.data.model.PrescriptionSection
import com.ddasum.app.data.model.ProgramRecommendation
import com.ddasum.app.data.model.ProgramSection
import com.ddasum.app.data.model.RegisteredMedication
import com.ddasum.app.data.model.WeeklyReport
import java.time.LocalDate
import java.time.LocalDateTime

fun WeeklyReportResponse.toDomain(): WeeklyReport = WeeklyReport(
    patientId = patientId,
    patientName = patientName,
    periodStart = LocalDate.parse(periodStart),
    periodEnd = LocalDate.parse(periodEnd),
    overallRate = overallRate,
    riskLevel = riskLevel,
    summaryText = summaryText,
    checklistInsight = checklistInsight,
    riskFlags = riskFlags,
    aiComments = aiComments,
    nextWeekTips = nextWeekTips,
    mealMissingCount = mealMissingCount.let {
        MealMissingCount(it.morning, it.lunch, it.dinner, it.total)
    },
    programSection = programSection.toDomain(),
    diagnosisSection = diagnosisSection?.let { DiagnosisSection(it.diagnosisTitle, it.diagnosisComment) },
    nextWeekStart = LocalDate.parse(nextWeekStart),
    dailyRates = dailyRates.map { DailyRate(LocalDate.parse(it.date), it.day, it.rate) },
    checklistRows = checklistRows.map { ChecklistRow(it.label, it.percent, it.percentStyle, it.dailyComments) },
    prescriptionSection = prescriptionSection.toDomain()
)

private fun ProgramSectionDto.toDomain(): ProgramSection = ProgramSection(
    activityTitle = activityTitle,
    activityDescription = activityDescription,
    effects = effects,
    recommendations = recommendations,
    categoryRecommendations = categoryRecommendations.map {
        ProgramRecommendation(
            category = it.category,
            reason = it.reason,
            hasProgram = it.hasProgram,
            postId = it.postId,
            programTitle = it.programTitle,
            programStartAt = it.programStartAt?.let { s -> LocalDateTime.parse(s) },
            noProgramMessage = it.noProgramMessage
        )
    }
)

private fun PrescriptionSectionDto.toDomain(): PrescriptionSection = PrescriptionSection(
    summaryText = summaryText,
    medicationHighlights = medicationHighlights,
    cautions = cautions,
    careTips = careTips,
    medications = medications.map {
        RegisteredMedication(
            medicationId = it.medicationId,
            prescriptionDate = LocalDate.parse(it.prescriptionDate),
            medicineSummary = it.medicineSummary
        )
    }
)
