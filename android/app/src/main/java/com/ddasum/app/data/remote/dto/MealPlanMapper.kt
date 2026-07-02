package com.ddasum.app.data.remote.dto

import com.ddasum.app.data.model.DietType
import com.ddasum.app.data.model.MealPlan
import com.ddasum.app.data.model.MealType
import java.time.LocalDate
import java.time.LocalDateTime

fun MealPlanResponse.toDomain(): MealPlan = MealPlan(
    id = mealPlanId,
    mealDate = LocalDate.parse(mealDate),
    mealType = MealType.valueOf(mealType),
    dietType = DietType.valueOf(dietType),
    menu = menu,
    calorie = calorie,
    protein = protein,
    createdAt = LocalDateTime.parse(createdAt)
)
