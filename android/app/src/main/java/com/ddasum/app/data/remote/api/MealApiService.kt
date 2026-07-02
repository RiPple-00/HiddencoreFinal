package com.ddasum.app.data.remote.api

import com.ddasum.app.data.remote.dto.MealPlanResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface MealApiService {

    @GET("meals/by-date")
    suspend fun getMealsByDate(
        @Query("date") date: String,
        @Query("facilityId") facilityId: Long? = null
    ): List<MealPlanResponse>

    @GET("meals/by-range")
    suspend fun getMealsByRange(
        @Query("startDate") startDate: String,
        @Query("endDate") endDate: String,
        @Query("facilityId") facilityId: Long? = null
    ): List<MealPlanResponse>
}
