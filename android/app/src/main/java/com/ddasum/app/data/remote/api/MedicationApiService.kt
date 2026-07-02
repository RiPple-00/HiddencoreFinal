package com.ddasum.app.data.remote.api

import com.ddasum.app.data.remote.dto.medication.MedicationDetailResponseDto
import com.ddasum.app.data.remote.dto.medication.MedicationHistoryItemDto
import com.ddasum.app.data.remote.dto.medication.MedicationScanRequestDto
import com.ddasum.app.data.remote.dto.medication.MedicationScanResponseDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.POST
import retrofit2.http.Query

interface MedicationApiService {

    /** Backend calls out to HIRA/MFDS synchronously — client sets a long timeout (~65s) for this. */
    @POST("medications/scan")
    suspend fun scanMedicationQr(@Body request: MedicationScanRequestDto): MedicationScanResponseDto

    @GET("medications/history")
    suspend fun getHistory(
        @Query("patientId") patientId: Long,
        @Query("guardianId") guardianId: Long? = null
    ): List<MedicationHistoryItemDto>

    @GET("medications/{medicationId}")
    suspend fun getDetail(@Path("medicationId") medicationId: Long): MedicationDetailResponseDto
}
