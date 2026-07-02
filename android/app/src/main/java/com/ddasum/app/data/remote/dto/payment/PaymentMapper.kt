package com.ddasum.app.data.remote.dto.payment

import com.ddasum.app.data.model.Invoice
import com.ddasum.app.data.model.InvoiceItem
import com.ddasum.app.data.model.PatientBillingSummary
import com.ddasum.app.data.model.PaymentRecord

fun StoragePatientDto.toDomain(): PatientBillingSummary = PatientBillingSummary(
    id = id,
    name = name,
    status = status,
    room = room,
    admissionDate = admissionDate,
    expectedTotal = expectedTotal,
    expectedTotalAsOf = expectedTotalAsOf
)

fun InvoiceItemDto.toDomain(): InvoiceItem = InvoiceItem(name = name, amount = amount)

fun InvoiceDto.toDomain(): Invoice = Invoice(
    id = id,
    month = month,
    title = title,
    issued = issued,
    due = due,
    amount = amount,
    status = status,
    tags = tags,
    period = period,
    dept = dept,
    covered = covered.map { it.toDomain() },
    nonCovered = nonCovered.map { it.toDomain() }
)

fun PaymentRecordDto.toDomain(): PaymentRecord = PaymentRecord(
    id = id,
    paidAt = paidAt,
    title = title,
    amount = amount,
    status = status,
    category = category,
    invoiceId = invoiceId
)
