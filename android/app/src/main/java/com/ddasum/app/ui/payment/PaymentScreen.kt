package com.ddasum.app.ui.payment

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ddasum.app.data.model.Invoice
import com.ddasum.app.data.model.PatientBillingSummary
import com.ddasum.app.data.model.PaymentRecord

@Composable
fun PaymentScreen(viewModel: PaymentViewModel = hiltViewModel()) {
    val uiState by viewModel.uiState.collectAsState()

    when {
        uiState.isLoading -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
        uiState.error != null -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text(text = uiState.error.orEmpty(), textAlign = TextAlign.Center, modifier = Modifier.padding(24.dp))
        }
        else -> LazyColumn(
            modifier = Modifier.fillMaxSize().padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item { BillingSummaryCard(uiState.billing) }
            item { Text(text = "청구서", style = MaterialTheme.typography.titleMedium) }
            items(uiState.invoices, key = { it.id }) { InvoiceRow(it) }
            item { Text(text = "결제 내역", style = MaterialTheme.typography.titleMedium) }
            items(uiState.payments, key = { it.id }) { PaymentRow(it) }
        }
    }
}

@Composable
private fun BillingSummaryCard(billing: PatientBillingSummary?) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            if (billing == null) {
                Text(text = "환자 정보를 불러오지 못했습니다.")
            } else {
                Text(text = billing.name, style = MaterialTheme.typography.titleMedium)
                Text(text = "${billing.room ?: "-"}호 · ${billing.status}")
                Text(
                    text = "예상 청구 총액 ${"%,d".format(billing.expectedTotal)}원",
                    style = MaterialTheme.typography.bodyLarge
                )
                billing.expectedTotalAsOf?.let { Text(text = "$it 기준") }
            }
        }
    }
}

@Composable
private fun InvoiceRow(invoice: Invoice) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text(text = invoice.title, style = MaterialTheme.typography.bodyLarge)
                Text(text = invoice.status)
            }
            Text(text = "${"%,d".format(invoice.amount)}원 · 납부기한 ${invoice.due ?: "-"}")
        }
    }
    HorizontalDivider()
}

@Composable
private fun PaymentRow(payment: PaymentRecord) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Text(text = payment.title)
            Text(text = payment.paidAt ?: "-", style = MaterialTheme.typography.bodySmall)
        }
        Text(text = "${"%,d".format(payment.amount)}원 · ${payment.status}")
    }
    HorizontalDivider()
}
