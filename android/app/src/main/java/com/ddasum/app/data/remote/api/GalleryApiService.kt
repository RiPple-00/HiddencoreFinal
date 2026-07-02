package com.ddasum.app.data.remote.api

import com.ddasum.app.data.remote.dto.ActivityGalleryListResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface GalleryApiService {

    @GET("guardian/me/patients/{patientId}/activity-gallery")
    suspend fun getActivityGallery(@Path("patientId") patientId: Long): ActivityGalleryListResponse
}
