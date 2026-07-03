package com.ddasum.app.data.repository

import com.ddasum.app.data.model.DietType
import com.ddasum.app.data.model.MealType
import com.ddasum.app.data.remote.api.MealApiService
import com.ddasum.app.data.remote.dto.MealPlanResponse
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test
import java.time.LocalDate

/**
 * @IoDispatcher was injected into the repository specifically so tests could hand it
 * a StandardTestDispatcher instead of the real Dispatchers.IO — no Hilt, no real network.
 */
class MealRepositoryImplTest {

    private val api: MealApiService = mockk()
    private val dispatcher = StandardTestDispatcher()
    private val repository = MealRepositoryImpl(api, dispatcher)

    @Test
    fun `getMealsByDate maps DTO fields to domain model correctly`() = runTest(dispatcher) {
        val date = LocalDate.of(2026, 7, 3)
        coEvery { api.getMealsByDate(date.toString(), null) } returns listOf(
            MealPlanResponse(
                mealPlanId = 1L,
                facilityId = 2L,
                adminId = 3L,
                mealDate = "2026-07-03",
                mealType = "BREAKFAST",
                dietType = "GENERAL",
                menu = "죽, 계란찜",
                calorie = 450,
                protein = 20,
                createdAt = "2026-07-03T08:00:00"
            )
        )

        val result = repository.getMealsByDate(date, facilityId = null)

        assertEquals(1, result.size)
        val meal = result.first()
        assertEquals(1L, meal.id)
        assertEquals(MealType.BREAKFAST, meal.mealType)
        assertEquals(DietType.GENERAL, meal.dietType)
        assertEquals("죽, 계란찜", meal.menu)
        assertEquals(450, meal.calorie)
        assertEquals(LocalDate.of(2026, 7, 3), meal.mealDate)
    }

    @Test
    fun `getMealsByDate returns empty list when server has no meals for that date`() = runTest(dispatcher) {
        val date = LocalDate.of(2026, 7, 4)
        coEvery { api.getMealsByDate(date.toString(), 5L) } returns emptyList()

        val result = repository.getMealsByDate(date, facilityId = 5L)

        assertEquals(emptyList<Any>(), result)
    }

    @Test
    fun `getMealsByRange formats both dates as ISO strings before calling the api`() = runTest(dispatcher) {
        val start = LocalDate.of(2026, 7, 1)
        val end = LocalDate.of(2026, 7, 7)
        // coEvery constrains the exact args MockK expects — if MealRepositoryImpl formatted
        // the dates differently, this stub wouldn't match and the test would fail with
        // "no answer found", so the assertion is implicit in the stub matching at all.
        coEvery { api.getMealsByRange("2026-07-01", "2026-07-07", null) } returns emptyList()

        val result = repository.getMealsByRange(start, end, facilityId = null)

        assertEquals(emptyList<Any>(), result)
    }
}
