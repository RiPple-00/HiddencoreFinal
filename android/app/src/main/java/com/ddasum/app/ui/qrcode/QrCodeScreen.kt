package com.ddasum.app.ui.qrcode

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun QrCodeScreen(viewModel: QrCodeViewModel = hiltViewModel()) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    var hasCameraPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) ==
                PackageManager.PERMISSION_GRANTED
        )
    }
    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted -> hasCameraPermission = granted }

    LaunchedEffect(Unit) {
        if (!hasCameraPermission) permissionLauncher.launch(Manifest.permission.CAMERA)
    }

    Box(modifier = Modifier.fillMaxSize()) {
        when {
            !hasCameraPermission -> Text(
                text = "처방전 QR 스캔을 위해 카메라 권한이 필요합니다.",
                modifier = Modifier.align(Alignment.Center).padding(24.dp),
                textAlign = TextAlign.Center
            )
            uiState is QrCodeUiState.Scanning ->
                QrCameraPreview(onQrDetected = viewModel::onQrDetected)

            uiState is QrCodeUiState.Loading -> Column(
                modifier = Modifier.align(Alignment.Center),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CircularProgressIndicator()
                Text(
                    text = "처방전 정보를 조회하는 중입니다. 최대 1분 정도 걸릴 수 있어요.",
                    modifier = Modifier.padding(top = 12.dp),
                    textAlign = TextAlign.Center
                )
            }

            uiState is QrCodeUiState.Success -> {
                val result = (uiState as QrCodeUiState.Success).result
                Column(
                    modifier = Modifier.align(Alignment.Center).padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = "스캔 완료", style = MaterialTheme.typography.titleMedium)
                    Text(text = "처방일: ${result.prescriptionDate}", modifier = Modifier.padding(top = 8.dp))
                    Text(text = result.medicineSummary ?: "요약 정보가 없습니다.", modifier = Modifier.padding(top = 4.dp))
                    Button(onClick = viewModel::reset, modifier = Modifier.padding(top = 16.dp)) {
                        Text("다시 스캔")
                    }
                }
            }

            uiState is QrCodeUiState.Error -> {
                val message = (uiState as QrCodeUiState.Error).message
                Column(
                    modifier = Modifier.align(Alignment.Center).padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = message, textAlign = TextAlign.Center)
                    Button(onClick = viewModel::reset, modifier = Modifier.padding(top = 16.dp)) {
                        Text("다시 시도")
                    }
                }
            }
        }
    }
}
