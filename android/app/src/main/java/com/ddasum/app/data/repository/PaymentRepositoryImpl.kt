package com.ddasum.app.data.repository

import com.ddasum.app.data.model.Invoice
import com.ddasum.app.data.model.PatientBillingSummary
import com.ddasum.app.data.model.PaymentRecord
import com.ddasum.app.data.remote.api.PaymentApiService
import com.ddasum.app.data.remote.dto.payment.toDomain
import com.ddasum.app.di.IoDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class PaymentRepositoryImpl @Inject constructor(
    private val api: PaymentApiService,
    @param:IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : PaymentRepository {

    override suspend fun getPatientBilling(patientId: Long?): PatientBillingSummary? =
        withContext(ioDispatcher) {
            api.getPatients(patientId).data.firstOrNull()?.toDomain()
        }

    override suspend fun getInvoices(patientId: Long?): List<Invoice> =
        withContext(ioDispatcher) {
            api.getInvoices(patientId).data.map { it.toDomain() }
        }

    override suspend fun getPayments(patientId: Long?): List<PaymentRecord> =
        withContext(ioDispatcher) {
            api.getPayments(patientId).data.map { it.toDomain() }
        }

    override suspend fun getOverdue(patientId: Long?, limit: Int?): List<PaymentRecord> =
        withContext(ioDispatcher) {
            api.getOverdue(patientId, limit).data.map { it.toDomain() }
        }
}
