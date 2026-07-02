package com.ddasum.app.data.remote.dto.medication

/**
 * Mirrors backend MedicationService.getMedicationDetail() (GET /api/medications/{id}).
 * MedicationDetail rows carry ~35 HIRA/MFDS/DUR enrichment fields server-side —
 * only the display-relevant subset is modeled here; extra JSON fields are safely
 * ignored by Gson, so this stays correct even though it isn't exhaustive.
 */
data class MedicationDetailResponseDto(
    val medicationId: Long,
    val patientId: Long,
    val guardianId: Long?,
    val prescriptionDate: String,
    val medicineSummary: String?,
    val details: List<MedicationDetailItemDto>
)

data class MedicationDetailItemDto(
    val medicationDetailId: Long,
    val itemSeq: String?,
    val medicineName: String?,
    val itemName: String?,
    val manufacturerName: String?,
    val unit: String?,
    val effect: String?,
    val useMethod: String?,
    val warning: String?,
    val caution: String?,
    val interaction: String?,
    val sideEffect: String?,
    val durInfoFound: Boolean?,
    val durWarningCount: Int?
)
