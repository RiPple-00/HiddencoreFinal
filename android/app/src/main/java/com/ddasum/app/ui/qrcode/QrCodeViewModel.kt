package com.ddasum.app.ui.qrcode

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ddasum.app.data.repository.AuthRepository
import com.ddasum.app.data.repository.GuardianRepository
import com.ddasum.app.data.repository.MedicationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class QrCodeViewModel @Inject constructor(
    private val medicationRepository: MedicationRepository,
    private val guardianRepository: GuardianRepository,
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<QrCodeUiState>(QrCodeUiState.Scanning)
    val uiState: StateFlow<QrCodeUiState> = _uiState.asStateFlow()

    // 카메라 analyzer가 프레임마다 계속 호출하므로, 한 번 스캔이 시작되면 재요청을 막는 가드
    private var isProcessing = false

    fun onQrDetected(rawValue: String) {
        if (isProcessing) return
        isProcessing = true
        _uiState.value = QrCodeUiState.Loading

        viewModelScope.launch {
            runCatching {
                val patients = guardianRepository.getLinkedPatients()
                val patientId = (patients.firstOrNull { it.isPrimary } ?: patients.firstOrNull())?.patientId
                    ?: error("연결된 환자가 없습니다.")
                val guardianId = authRepository.currentGuardianId()
                medicationRepository.scanQr(patientId = patientId, guardianId = guardianId, qrRawData = rawValue)
            }.onSuccess { result ->
                _uiState.value = QrCodeUiState.Success(result)
            }.onFailure { e ->
                isProcessing = false
                _uiState.value = QrCodeUiState.Error(e.message ?: "QR 인식에 실패했습니다.")
            }
        }
    }

    fun reset() {
        isProcessing = false
        _uiState.value = QrCodeUiState.Scanning
    }
}
