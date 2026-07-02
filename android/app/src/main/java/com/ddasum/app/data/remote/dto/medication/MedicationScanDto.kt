package com.ddasum.app.data.remote.dto.medication

/** Mirrors backend MedicationDto (POST /api/medications/scan). */
data class MedicationScanRequestDto(
    val patientId: Long,
    val guardianId: Long?,
    val qrRawData: String
)

data class MedicationScanResponseDto(
    val medicationId: Long,
    val patientId: Long,
    val guardianId: Long?,
    val prescriptionDate: String,
    val medicineSummary: String?,
    /** JSON-encoded string (not a nested object) — caller must parse it separately if needed. */
    val medicineData: String?
)
