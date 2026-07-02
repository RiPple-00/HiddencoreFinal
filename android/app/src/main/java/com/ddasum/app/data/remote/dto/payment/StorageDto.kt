package com.ddasum.app.data.remote.dto.payment

/** All /api/storage endpoints wrap their payload as {"data": [...]}. */
data class StoragePatientsResponse(val data: List<StoragePatientDto>)
data class StorageInvoicesResponse(val data: List<InvoiceDto>)
data class StoragePaymentsResponse(val data: List<PaymentRecordDto>)

data class StoragePatientDto(
    val id: Long,
    val name: String,
    val status: String,
    val room: String?,
    val admissionDate: String?,
    val expectedTotal: Long,
    val expectedTotalAsOf: String?
)

data class InvoiceDto(
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
    val covered: List<InvoiceItemDto>,
    val nonCovered: List<InvoiceItemDto>
)

data class InvoiceItemDto(val name: String, val amount: Long)

data class PaymentRecordDto(
    val id: Long,
    val paidAt: String?,
    val title: String,
    val amount: Long,
    val status: String,
    val category: String?,
    val invoiceId: Long?
)
