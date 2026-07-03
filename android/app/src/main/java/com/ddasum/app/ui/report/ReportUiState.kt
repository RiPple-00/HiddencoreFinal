package com.ddasum.app.ui.report

import com.ddasum.app.data.model.WeeklyReport

data class ReportUiState(
    val isLoading: Boolean = true,
    val report: WeeklyReport? = null,
    val error: String? = null
)
