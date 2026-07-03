package com.ddasum.app.ui.payment

import com.ddasum.app.data.model.Invoice
import com.ddasum.app.data.model.PatientBillingSummary
import com.ddasum.app.data.model.PaymentRecord

data class PaymentUiState(
    val isLoading: Boolean = true,
    val billing: PatientBillingSummary? = null,
    val invoices: List<Invoice> = emptyList(),
    val payments: List<PaymentRecord> = emptyList(),
    val error: String? = null
)
