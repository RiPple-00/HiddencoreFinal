package com.ddasum.app.data.remote.dto

import com.ddasum.app.data.model.ActivityGallery
import com.ddasum.app.data.model.GalleryPhoto
import java.time.LocalDateTime

/**
 * imageUrl comes back as a server-relative path (e.g. "/uploads/gallery/x.jpg"),
 * served as a static file outside the "/api" prefix — fileBaseUrl is the API
 * base URL with the trailing "api/" stripped off.
 */
fun GalleryCardDto.toDomain(fileBaseUrl: String): GalleryPhoto = GalleryPhoto(
    id = documentId,
    imageUrl = fileBaseUrl.trimEnd('/') + imageUrl,
    title = title,
    content = content,
    uploadedAt = LocalDateTime.parse(uploadedAt)
)

fun ActivityGalleryListResponse.toDomain(fileBaseUrl: String): ActivityGallery = ActivityGallery(
    slotCapacity = slotCapacity,
    filledCount = filledCount,
    photos = slots.map { it.toDomain(fileBaseUrl) },
    hero = hero?.toDomain(fileBaseUrl),
    today = today.map { it.toDomain(fileBaseUrl) },
    week = week.map { it.toDomain(fileBaseUrl) }
)
