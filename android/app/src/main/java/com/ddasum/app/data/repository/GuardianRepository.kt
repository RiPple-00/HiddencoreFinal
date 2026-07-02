package com.ddasum.app.data.repository

import com.ddasum.app.data.model.LinkedPatient

interface GuardianRepository {
    suspend fun getLinkedPatients(): List<LinkedPatient>
}
