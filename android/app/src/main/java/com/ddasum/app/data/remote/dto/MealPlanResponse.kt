package com.ddasum.app.data.remote.dto

/**
 * 백엔드 MealPlanDto.MealPlanResponse를 그대로 반영 (/api/meals/by-date, /api/meals/by-range).
 * mealDate: "yyyy-MM-dd" 형식, createdAt: ISO LocalDateTime 문자열.
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
