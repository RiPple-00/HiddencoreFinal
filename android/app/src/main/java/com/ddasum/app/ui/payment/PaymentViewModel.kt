package com.ddasum.app.ui.payment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ddasum.app.data.repository.GuardianRepository
import com.ddasum.app.data.repository.PaymentRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PaymentViewModel @Inject constructor(
    private val paymentRepository: PaymentRepository,
    private val guardianRepository: GuardianRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(PaymentUiState())
    val uiState: StateFlow<PaymentUiState> = _uiState.asStateFlow()

    init {
        load()
    }

    fun load() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            runCatching {
                val patients = guardianRepository.getLinkedPatients()
                val patientId = (patients.firstOrNull { it.isPrimary } ?: patients.firstOrNull())?.patientId

                val billingDeferred = async { paymentRepository.getPatientBilling(patientId) }
                val invoicesDeferred = async { paymentRepository.getInvoices(patientId) }
                val paymentsDeferred = async { paymentRepository.getPayments(patientId) }
                Triple(billingDeferred.await(), invoicesDeferred.await(), paymentsDeferred.await())
            }.onSuccess { (billing, invoices, payments) ->
                _uiState.update {
                    it.copy(isLoading = false, billing = billing, invoices = invoices, payments = payments)
                }
            }.onFailure { e ->
                _uiState.update { it.copy(isLoading = false, error = e.message ?: "수납 정보를 불러오지 못했습니다.") }
            }
        }
    }
}
