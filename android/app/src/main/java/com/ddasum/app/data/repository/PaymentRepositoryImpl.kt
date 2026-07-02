package com.ddasum.app.data.repository

import com.ddasum.app.data.model.Invoice
import com.ddasum.app.data.model.PatientBillingSummary
import com.ddasum.app.data.model.PaymentRecord
import com.ddasum.app.data.remote.api.PaymentApiService
import com.ddasum.app.data.remote.dto.payment.toDomain
import javax.inject.Inject

class PaymentRepositoryImpl @Inject constructor(
    private val api: PaymentApiService
) : PaymentRepository {

    override suspend fun getPatientBilling(patientId: Long?): PatientBillingSummary? =
        api.getPatients(patientId).data.firstOrNull()?.toDomain()

    override suspend fun getInvoices(patientId: Long?): List<Invoice> =
        api.getInvoices(patientId).data.map { it.toDomain() }

    override suspend fun getPayments(patientId: Long?): List<PaymentRecord> =
        api.getPayments(patientId).data.map { it.toDomain() }

    override suspend fun getOverdue(patientId: Long?, limit: Int?): List<PaymentRecord> =
        api.getOverdue(patientId, limit).data.map { it.toDomain() }
}
