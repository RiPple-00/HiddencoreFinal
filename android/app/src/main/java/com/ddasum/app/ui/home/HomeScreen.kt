package com.ddasum.app.ui.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun HomeScreen(
    onPaymentClick: () -> Unit,
    onReportClick: () -> Unit,
    onChatbotClick: () -> Unit,
    onQrCodeClick: () -> Unit,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        MealPlanSection(
            meals = uiState.meals,
            isLoading = uiState.isLoading,
            error = uiState.error
        )
        GallerySection(photos = uiState.galleryPhotos)
        ActionButtonsSection(
            onPaymentClick = onPaymentClick,
            onReportClick = onReportClick,
            onChatbotClick = onChatbotClick,
            onQrCodeClick = onQrCodeClick
        )
    }
}
