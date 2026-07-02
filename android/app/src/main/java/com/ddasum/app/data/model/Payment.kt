package com.ddasum.app.data.model

data class PatientBillingSummary(
    val id: Long,
    val name: String,
    val status: String,
    val room: String?,
    val admissionDate: String?,
    val expectedTotal: Long,
    val expectedTotalAsOf: String?
)

data class Invoice(
    val id: Long,
    val month: String,
    val title: String,
    val issued: String?,
    val due: String?,
    val amount: Long,
    val status: String,
    val tags: List<String>,
    val period: String?,
    val dept: String?,
    val covered: List<InvoiceItem>,
    val nonCovered: List<InvoiceItem>
)

data class InvoiceItem(val name: String, val amount: Long)

data class PaymentRecord(
    val id: Long,
    val paidAt: String?,
    val title: String,
    val amount: Long,
    val status: String,
    val category: String?,
    val invoiceId: Long?
)
