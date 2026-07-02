package com.ddasum.app.data.remote.dto.medication

import com.ddasum.app.data.model.MedicationDetail
import com.ddasum.app.data.model.MedicationDrugDetail
import com.ddasum.app.data.model.MedicationHistoryItem
import com.ddasum.app.data.model.MedicationScanResult
import java.time.LocalDate
import java.time.LocalDateTime

fun MedicationScanResponseDto.toDomain(): MedicationScanResult = MedicationScanResult(
    medicationId = medicationId,
    patientId = patientId,
    prescriptionDate = LocalDate.parse(prescriptionDate),
    medicineSummary = medicineSummary,
    medicineDataJson = medicineData
)

fun MedicationHistoryItemDto.toDomain(): MedicationHistoryItem = MedicationHistoryItem(
    medicationId = medicationId,
    prescriptionDate = LocalDate.parse(prescriptionDate),
    medicineSummary = medicineSummary,
    createdAt = LocalDateTime.parse(createdAt)
)

fun MedicationDetailResponseDto.toDomain(): MedicationDetail = MedicationDetail(
    medicationId = medicationId,
    prescriptionDate = LocalDate.parse(prescriptionDate),
    medicineSummary = medicineSummary,
    drugs = details.map { it.toDomain() }
)

private fun MedicationDetailItemDto.toDomain(): MedicationDrugDetail = MedicationDrugDetail(
    medicineName = medicineName,
    itemName = itemName,
    manufacturerName = manufacturerName,
    effect = effect,
    useMethod = useMethod,
    warning = warning,
    caution = caution,
    interaction = interaction,
    sideEffect = sideEffect,
    durInfoFound = durInfoFound ?: false,
    durWarningCount = durWarningCount ?: 0
)
