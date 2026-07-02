package com.ddasum.app.data.remote.api

import com.ddasum.app.data.remote.dto.guardian.GuardianLinkedPatientDto
import retrofit2.http.GET

interface GuardianApiService {

    @GET("guardian/me/linked-patients")
    suspend fun getLinkedPatients(): List<GuardianLinkedPatientDto>
}
