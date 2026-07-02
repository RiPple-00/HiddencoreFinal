package com.ddasum.app.data.repository

import com.ddasum.app.data.model.MealPlan
import java.time.LocalDate

interface MealRepository {
    suspend fun getMealsByDate(date: LocalDate, facilityId: Long? = null): List<MealPlan>
    suspend fun getMealsByRange(startDate: LocalDate, endDate: LocalDate, facilityId: Long? = null): List<MealPlan>
}
