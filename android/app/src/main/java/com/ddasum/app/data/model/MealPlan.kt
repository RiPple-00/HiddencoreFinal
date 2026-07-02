package com.ddasum.app.data.model

import java.time.LocalDate
import java.time.LocalDateTime

enum class MealType { BREAKFAST, LUNCH, DINNER }

enum class DietType {
    GENERAL, LOW_SODIUM, DIABETIC, SOFT, LIQUID, HIGH_PROTEIN, LOW_FAT, RENAL, ETC
}

data class MealPlan(
    val id: Long,
    val mealDate: LocalDate,
    val mealType: MealType,
    val dietType: DietType,
    val menu: String,
    val calorie: Int,
    val protein: Int,
    val createdAt: LocalDateTime
)
