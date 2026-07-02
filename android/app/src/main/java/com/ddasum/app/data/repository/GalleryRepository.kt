package com.ddasum.app.data.repository

import com.ddasum.app.data.model.ActivityGallery

interface GalleryRepository {
    suspend fun getActivityGallery(patientId: Long): ActivityGallery
}
