package com.ddasum.app.data.repository

import com.ddasum.app.BuildConfig
import com.ddasum.app.data.model.ActivityGallery
import com.ddasum.app.data.remote.api.GalleryApiService
import com.ddasum.app.data.remote.dto.toDomain
import com.ddasum.app.di.IoDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GalleryRepositoryImpl @Inject constructor(
    private val api: GalleryApiService,
    @param:IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : GalleryRepository {

    private val fileBaseUrl = BuildConfig.BASE_URL.removeSuffix("api/")

    override suspend fun getActivityGallery(patientId: Long): ActivityGallery =
        withContext(ioDispatcher) {
            api.getActivityGallery(patientId).toDomain(fileBaseUrl)
        }
}
