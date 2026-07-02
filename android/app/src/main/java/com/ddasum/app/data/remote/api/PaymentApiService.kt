package com.ddasum.app.data.remote.api

import com.ddasum.app.data.remote.dto.payment.StorageInvoicesResponse
import com.ddasum.app.data.remote.dto.payment.StoragePatientsResponse
import com.ddasum.app.data.remote.dto.payment.StoragePaymentsResponse
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Backend note: /api/storage is a demo/mock service (StorageDemoService) —
 * only "invoices" is partially DB-backed, "payments"/"overdue" always return
 * the same fixed rows regardless of patientId. POST endpoints (pay/cancel)
 * don't persist anything server-side, so they're intentionally not modeled here.
 */
interface PaymentApiService {

    @GET("storage/patients")
    suspend fun getPatients(@Query("id") id: Long? = null): StoragePatientsResponse

    @GET("storage/invoices")
    suspend fun getInvoices(@Query("patientId") patientId: Long? = null): StorageInvoicesResponse

    @GET("storage/payments")
    suspend fun getPayments(@Query("patientId") patientId: Long? = null): StoragePaymentsResponse

    @GET("storage/overdue")
    suspend fun getOverdue(
        @Query("patientId") patientId: Long? = null,
        @Query("limit") limit: Int? = null
    ): StoragePaymentsResponse
}
