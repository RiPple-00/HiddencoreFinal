package com.ddasum.app.data.remote.dto

/**
 * Mirrors backend ActivityGalleryListResponse
 * (GET /api/guardian/me/patients/{patientId}/activity-gallery).
 */
data class ActivityGalleryListResponse(
    val slotCapacity: Int,
    val filledCount: Int,
    val slots: List<GalleryCardDto>,
    val hero: GalleryCardDto?,
    val today: List<GalleryCardDto>,
    val week: List<GalleryCardDto>
)
