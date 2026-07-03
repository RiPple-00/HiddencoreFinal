package com.ddasum.app.ui.qrcode

import com.ddasum.app.data.model.MedicationScanResult

sealed class QrCodeUiState {
    data object Scanning : QrCodeUiState()
    data object Loading : QrCodeUiState()
    data class Success(val result: MedicationScanResult) : QrCodeUiState()
    data class Error(val message: String) : QrCodeUiState()
}
