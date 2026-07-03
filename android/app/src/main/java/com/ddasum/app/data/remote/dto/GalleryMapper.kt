package com.ddasum.app.data.remote.dto

import com.ddasum.app.data.model.ActivityGallery
import com.ddasum.app.data.model.GalleryPhoto
import java.time.LocalDateTime

/**
 * imageUrl은 서버 기준 상대경로("/uploads/gallery/x.jpg" 형태)로 내려오고, "/api" 접두사
 * 밖에서 정적 파일로 서빙된다 — fileBaseUrl은 API base URL에서 끝의 "api/"를 뗀 값이다.
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
