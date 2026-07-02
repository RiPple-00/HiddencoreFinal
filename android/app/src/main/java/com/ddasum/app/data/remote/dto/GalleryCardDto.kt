package com.ddasum.app.data.remote.dto

/** Mirrors backend GalleryModalDto — one photo card. */
data class GalleryCardDto(
    val documentId: Long,
    val imageUrl: String,
    val title: String,
    val content: String,
    val uploadedAt: String
)
