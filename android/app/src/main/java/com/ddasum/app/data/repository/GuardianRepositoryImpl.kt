package com.ddasum.app.data.repository

import com.ddasum.app.data.model.LinkedPatient
import com.ddasum.app.data.remote.api.GuardianApiService
import javax.inject.Inject

class GuardianRepositoryImpl @Inject constructor(
    private val api: GuardianApiService
) : GuardianRepository {

    override suspend fun getLinkedPatients(): List<LinkedPatient> =
        api.getLinkedPatients().map {
            LinkedPatient(
                patientId = it.patientId,
                patientName = it.patientName,
                relationship = it.relationship,
                isPrimary = it.primary
            )
        }
}
