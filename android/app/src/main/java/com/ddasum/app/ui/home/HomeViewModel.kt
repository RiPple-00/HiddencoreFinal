package com.ddasum.app.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ddasum.app.data.repository.GalleryRepository
import com.ddasum.app.data.repository.GuardianRepository
import com.ddasum.app.data.repository.MealRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val mealRepository: MealRepository,
    private val galleryRepository: GalleryRepository,
    private val guardianRepository: GuardianRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        loadHome()
    }

    fun loadHome() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            runCatching {
                val patients = guardianRepository.getLinkedPatients()
                val primaryPatientId = (patients.firstOrNull { it.isPrimary } ?: patients.firstOrNull())?.patientId

                val mealsDeferred = async { mealRepository.getMealsByDate(LocalDate.now()) }
                val galleryDeferred = async {
                    primaryPatientId?.let { galleryRepository.getActivityGallery(it).photos } ?: emptyList()
                }
                mealsDeferred.await() to galleryDeferred.await()
            }.onSuccess { (meals, photos) ->
                _uiState.update { it.copy(isLoading = false, meals = meals, galleryPhotos = photos) }
            }.onFailure { e ->
                _uiState.update { it.copy(isLoading = false, error = e.message ?: "홈 정보를 불러오지 못했습니다.") }
            }
        }
    }
}
