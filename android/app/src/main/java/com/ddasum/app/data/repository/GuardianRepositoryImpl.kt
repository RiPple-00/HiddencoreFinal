package com.ddasum.app.data.repository

import com.ddasum.app.data.model.LinkedPatient
import com.ddasum.app.data.remote.api.GuardianApiService
import com.ddasum.app.di.IoDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GuardianRepositoryImpl @Inject constructor(
    private val api: GuardianApiService,
    @param:IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : GuardianRepository {

    override suspend fun getLinkedPatients(): List<LinkedPatient> =
        withContext(ioDispatcher) {
            api.getLinkedPatients().map {
                LinkedPatient(
                    patientId = it.patientId,
                    patientName = it.patientName,
                    relationship = it.relationship,
                    isPrimary = it.primary
                )
            }
        }
}
