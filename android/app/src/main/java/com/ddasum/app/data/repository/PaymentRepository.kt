package com.ddasum.app.data.repository

import com.ddasum.app.data.model.Invoice
import com.ddasum.app.data.model.PatientBillingSummary
import com.ddasum.app.data.model.PaymentRecord

interface PaymentRepository {
    suspend fun getPatientBilling(patientId: Long?): PatientBillingSummary?
    suspend fun getInvoices(patientId: Long?): List<Invoice>
    suspend fun getPayments(patientId: Long?): List<PaymentRecord>
    suspend fun getOverdue(patientId: Long?, limit: Int? = null): List<PaymentRecord>
}
