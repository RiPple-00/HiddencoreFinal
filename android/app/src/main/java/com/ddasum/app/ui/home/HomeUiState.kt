package com.ddasum.app.ui.home

import com.ddasum.app.data.model.GalleryPhoto
import com.ddasum.app.data.model.MealPlan

data class HomeUiState(
    val isLoading: Boolean = true,
    val meals: List<MealPlan> = emptyList(),
    val galleryPhotos: List<GalleryPhoto> = emptyList(),
    val error: String? = null
)
