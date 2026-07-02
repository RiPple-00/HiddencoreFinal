package com.ddasum.app.data.remote.dto.guardian

/** Mirrors backend GuardianLinkedPatientResponse (GET /api/guardian/me/linked-patients). */
data class GuardianLinkedPatientDto(
    val patientId: Long,
    val patientName: String,
    val relationship: String,
    val primary: Boolean
)
