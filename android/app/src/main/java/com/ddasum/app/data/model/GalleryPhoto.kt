package com.ddasum.app.data.model

import java.time.LocalDateTime

data class GalleryPhoto(
    val id: Long,
    val imageUrl: String,
    val title: String,
    val content: String,
    val uploadedAt: LocalDateTime
)

data class ActivityGallery(
    val slotCapacity: Int,
    val filledCount: Int,
    val photos: List<GalleryPhoto>,
    val hero: GalleryPhoto?,
    val today: List<GalleryPhoto>,
    val week: List<GalleryPhoto>
)
