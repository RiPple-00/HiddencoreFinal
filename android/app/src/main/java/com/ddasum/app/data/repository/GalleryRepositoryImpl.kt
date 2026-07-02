package com.ddasum.app.data.repository

import com.ddasum.app.BuildConfig
import com.ddasum.app.data.model.ActivityGallery
import com.ddasum.app.data.remote.api.GalleryApiService
import com.ddasum.app.data.remote.dto.toDomain
import javax.inject.Inject

class GalleryRepositoryImpl @Inject constructor(
    private val api: GalleryApiService
) : GalleryRepository {

    private val fileBaseUrl = BuildConfig.BASE_URL.removeSuffix("api/")

    override suspend fun getActivityGallery(patientId: Long): ActivityGallery =
        api.getActivityGallery(patientId).toDomain(fileBaseUrl)
}
