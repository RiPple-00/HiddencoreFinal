package com.ddasum.app.data.remote.dto

/**
 * Mirrors backend MealPlanDto.MealPlanResponse (/api/meals/by-date, /api/meals/by-range).
 * mealDate: "yyyy-MM-dd", createdAt: ISO LocalDateTime string.
 */
data class MealPlanResponse(
    val mealPlanId: Long,
    val facilityId: Long,
    val adminId: Long,
    val mealDate: String,
    val mealType: String,
    val dietType: String,
    val menu: String,
    val calorie: Int,
    val protein: Int,
    val createdAt: String
)
