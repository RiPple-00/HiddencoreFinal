package com.ddasum.app.ui.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ddasum.app.data.model.MealPlan
import com.ddasum.app.data.model.MealType

private fun MealType.label(): String = when (this) {
    MealType.BREAKFAST -> "아침"
    MealType.LUNCH -> "점심"
    MealType.DINNER -> "저녁"
}

@Composable
fun MealPlanSection(
    meals: List<MealPlan>,
    isLoading: Boolean,
    error: String?,
    modifier: Modifier = Modifier
) {
    Card(modifier = modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = "오늘의 식단", style = MaterialTheme.typography.titleMedium)
            when {
                isLoading -> CircularProgressIndicator(modifier = Modifier.padding(top = 8.dp))
                error != null -> Text(text = error, modifier = Modifier.padding(top = 8.dp))
                meals.isEmpty() -> Text(text = "등록된 식단이 없습니다.", modifier = Modifier.padding(top = 8.dp))
                else -> meals.sortedBy { it.mealType.ordinal }.forEach { meal ->
                    Text(
                        text = "${meal.mealType.label()} · ${meal.menu} (${meal.calorie}kcal)",
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
            }
        }
    }
}
