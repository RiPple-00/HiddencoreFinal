package com.ddasum.app.data.remote.dto.medication

/** Mirrors backend MedicationService.getMedicationHistory() row (GET /api/medications/history). */
data class MedicationHistoryItemDto(
    val medicationId: Long,
    val patientId: Long,
    val guardianId: Long?,
    val prescriptionDate: String,
    val medicineSummary: String?,
    val createdAt: String
)
