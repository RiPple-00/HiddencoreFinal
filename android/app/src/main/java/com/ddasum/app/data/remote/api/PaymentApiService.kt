package com.ddasum.app.data.remote.api

import com.ddasum.app.data.remote.dto.payment.StorageInvoicesResponse
import com.ddasum.app.data.remote.dto.payment.StoragePatientsResponse
import com.ddasum.app.data.remote.dto.payment.StoragePaymentsResponse
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * 백엔드 참고: /api/storage는 데모/목(mock) 서비스다(StorageDemoService) — "invoices"만
 * 부분적으로 DB에 연결돼 있고, "payments"/"overdue"는 patientId와 무관하게 항상 같은
 * 고정 데이터를 반환한다. POST 엔드포인트(pay/cancel)는 서버에 아무것도 저장하지 않아서
 * 여기서는 의도적으로 모델링하지 않았다.
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
