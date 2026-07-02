package com.ddasum.app.data.repository

import com.ddasum.app.data.model.MealPlan
import com.ddasum.app.data.remote.api.MealApiService
import com.ddasum.app.data.remote.dto.toDomain
import java.time.LocalDate
import javax.inject.Inject

class MealRepositoryImpl @Inject constructor(
    private val api: MealApiService
) : MealRepository {

    override suspend fun getMealsByDate(date: LocalDate, facilityId: Long?): List<MealPlan> =
        api.getMealsByDate(date.toString(), facilityId).map { it.toDomain() }

    override suspend fun getMealsByRange(
        startDate: LocalDate,
        endDate: LocalDate,
        facilityId: Long?
    ): List<MealPlan> =
        api.getMealsByRange(startDate.toString(), endDate.toString(), facilityId).map { it.toDomain() }
}
