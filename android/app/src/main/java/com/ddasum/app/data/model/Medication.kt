package com.ddasum.app.data.model

import java.time.LocalDate
import java.time.LocalDateTime

data class MedicationScanResult(
    val medicationId: Long,
    val patientId: Long,
    val prescriptionDate: LocalDate,
    val medicineSummary: String?,
    val medicineDataJson: String?
)

data class MedicationHistoryItem(
    val medicationId: Long,
    val prescriptionDate: LocalDate,
    val medicineSummary: String?,
    val createdAt: LocalDateTime
)

data class MedicationDetail(
    val medicationId: Long,
    val prescriptionDate: LocalDate,
    val medicineSummary: String?,
    val drugs: List<MedicationDrugDetail>
)

data class MedicationDrugDetail(
    val medicineName: String?,
    val itemName: String?,
    val manufacturerName: String?,
    val effect: String?,
    val useMethod: String?,
    val warning: String?,
    val caution: String?,
    val interaction: String?,
    val sideEffect: String?,
    val durInfoFound: Boolean,
    val durWarningCount: Int
)
