package com.ddasum.app.ui.report

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ddasum.app.data.repository.GuardianRepository
import com.ddasum.app.data.repository.ReportRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.DayOfWeek
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class ReportViewModel @Inject constructor(
    private val reportRepository: ReportRepository,
    private val guardianRepository: GuardianRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ReportUiState())
    val uiState: StateFlow<ReportUiState> = _uiState.asStateFlow()

    init {
        load()
    }

    fun load() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            runCatching {
                val patients = guardianRepository.getLinkedPatients()
                val patientId = (patients.firstOrNull { it.isPrimary } ?: patients.firstOrNull())?.patientId
                    ?: error("연결된 환자가 없습니다.")

                // 리포트는 "완결된" 주(월~일)만 조회 가능 — 지난주 월요일~일요일을 명시적으로 계산해서 넘긴다.
                val thisMonday = LocalDate.now().with(DayOfWeek.MONDAY)
                val lastMonday = thisMonday.minusWeeks(1)
                val lastSunday = lastMonday.plusDays(6)

                reportRepository.getWeeklyReport(
                    patientId = patientId,
                    startDate = lastMonday,
                    endDate = lastSunday
                )
            }.onSuccess { report ->
                _uiState.update { it.copy(isLoading = false, report = report) }
            }.onFailure { e ->
                _uiState.update { it.copy(isLoading = false, error = e.message ?: "리포트를 불러오지 못했습니다.") }
            }
        }
    }
}
