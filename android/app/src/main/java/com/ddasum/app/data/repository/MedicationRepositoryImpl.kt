package com.ddasum.app.data.repository

import com.ddasum.app.data.model.MedicationDetail
import com.ddasum.app.data.model.MedicationHistoryItem
import com.ddasum.app.data.model.MedicationScanResult
import com.ddasum.app.data.remote.api.MedicationApiService
import com.ddasum.app.data.remote.dto.medication.MedicationScanRequestDto
import com.ddasum.app.data.remote.dto.medication.toDomain
import javax.inject.Inject

class MedicationRepositoryImpl @Inject constructor(
    private val api: MedicationApiService
) : MedicationRepository {

    override suspend fun scanQr(patientId: Long, guardianId: Long?, qrRawData: String): MedicationScanResult =
        api.scanMedicationQr(MedicationScanRequestDto(patientId, guardianId, qrRawData)).toDomain()

    override suspend fun getHistory(patientId: Long, guardianId: Long?): List<MedicationHistoryItem> =
        api.getHistory(patientId, guardianId).map { it.toDomain() }

    override suspend fun getDetail(medicationId: Long): MedicationDetail =
        api.getDetail(medicationId).toDomain()
}
