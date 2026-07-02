package com.ddasum.app.data.repository

import com.ddasum.app.data.model.MedicationDetail
import com.ddasum.app.data.model.MedicationHistoryItem
import com.ddasum.app.data.model.MedicationScanResult

interface MedicationRepository {
    suspend fun scanQr(patientId: Long, guardianId: Long?, qrRawData: String): MedicationScanResult
    suspend fun getHistory(patientId: Long, guardianId: Long?): List<MedicationHistoryItem>
    suspend fun getDetail(medicationId: Long): MedicationDetail
}
