package com.ddasum.app.data.remote.dto.report

/** Mirrors backend GuardianWeeklyCareReportResponse (field names verified against source). */
data class WeeklyReportResponse(
    val patientId: Long,
    val patientName: String,
    val periodStart: String,
    val periodEnd: String,
    val overallRate: Int,
    val riskLevel: String,
    val summaryText: String,
    val checklistInsight: String,
    val riskFlags: List<String>,
    val aiComments: List<String>,
    val nextWeekTips: List<String>,
    val mealMissingCount: MealMissingCountDto,
    val programSection: ProgramSectionDto,
    val diagnosisSection: DiagnosisSectionDto?,
    val nextWeekStart: String,
    val dailyRates: List<DailyRateDto>,
    val checklistRows: List<ChecklistRowDto>,
    val prescriptionSection: PrescriptionSectionDto
)

data class MealMissingCountDto(
    val morning: Int,
    val lunch: Int,
    val dinner: Int,
    val total: Int
)

data class ProgramSectionDto(
    val activityTitle: String?,
    val activityDescription: String?,
    val effects: List<String>,
    val recommendations: List<String>,
    val categoryRecommendations: List<ProgramRecommendationDto>
)

data class ProgramRecommendationDto(
    val category: String,
    val reason: String?,
    val hasProgram: Boolean,
    val postId: Long?,
    val programTitle: String?,
    val programStartAt: String?,
    val noProgramMessage: String?
)

data class DiagnosisSectionDto(
    val diagnosisTitle: String?,
    val diagnosisComment: String?
)

data class DailyRateDto(
    val date: String,
    val day: String,
    val rate: Int
)

data class ChecklistRowDto(
    val label: String,
    val percent: Int,
    val percentStyle: String,
    val dailyComments: List<String>
)

data class PrescriptionSectionDto(
    val summaryText: String?,
    val medicationHighlights: List<String>,
    val cautions: List<String>,
    val careTips: List<String>,
    val medications: List<RegisteredMedicationDto>
)

data class RegisteredMedicationDto(
    val medicationId: Long,
    val prescriptionDate: String,
    val medicineSummary: String?
)
