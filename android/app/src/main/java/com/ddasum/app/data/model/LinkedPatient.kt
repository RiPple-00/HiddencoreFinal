package com.ddasum.app.data.model

data class LinkedPatient(
    val patientId: Long,
    val patientName: String,
    val relationship: String,
    val isPrimary: Boolean
)
