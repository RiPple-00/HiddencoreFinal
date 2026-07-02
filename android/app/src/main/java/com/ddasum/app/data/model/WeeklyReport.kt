package com.ddasum.app.data.model

import java.time.LocalDate
import java.time.LocalDateTime

data class WeeklyReport(
    val patientId: Long,
    val patientName: String,
    val periodStart: LocalDate,
    val periodEnd: LocalDate,
    val overallRate: Int,
    val riskLevel: String,
    val summaryText: String,
    val checklistInsight: String,
    val riskFlags: List<String>,
    val aiComments: List<String>,
    val nextWeekTips: List<String>,
    val mealMissingCount: MealMissingCount,
    val programSection: ProgramSection,
    val diagnosisSection: DiagnosisSection?,
    val nextWeekStart: LocalDate,
    val dailyRates: List<DailyRate>,
    val checklistRows: List<ChecklistRow>,
    val prescriptionSection: PrescriptionSection
)

data class MealMissingCount(val morning: Int, val lunch: Int, val dinner: Int, val total: Int)

data class ProgramSection(
    val activityTitle: String?,
    val activityDescription: String?,
    val effects: List<String>,
    val recommendations: List<String>,
    val categoryRecommendations: List<ProgramRecommendation>
)

data class ProgramRecommendation(
    val category: String,
    val reason: String?,
    val hasProgram: Boolean,
    val postId: Long?,
    val programTitle: String?,
    val programStartAt: LocalDateTime?,
    val noProgramMessage: String?
)

data class DiagnosisSection(val diagnosisTitle: String?, val diagnosisComment: String?)

data class DailyRate(val date: LocalDate, val day: String, val rate: Int)

data class ChecklistRow(
    val label: String,
    val percent: Int,
    val percentStyle: String,
    val dailyComments: List<String>
)

data class PrescriptionSection(
    val summaryText: String?,
    val medicationHighlights: List<String>,
    val cautions: List<String>,
    val careTips: List<String>,
    val medications: List<RegisteredMedication>
)

data class RegisteredMedication(
    val medicationId: Long,
    val prescriptionDate: LocalDate,
    val medicineSummary: String?
)
