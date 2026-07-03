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
 * Repository에 @IoDispatcher를 주입해둔 건 바로 이럴 때를 위해서다 — 테스트에서
 * 진짜 Dispatchers.IO 대신 StandardTestDispatcher를 넘겨줄 수 있다. Hilt도, 진짜 네트워크도 필요 없다.
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
        // coEvery는 MockK가 기대하는 인자를 정확히 못박는다 — MealRepositoryImpl이 날짜를
        // 다르게 포맷했다면 이 스텁이 매칭이 안 돼서 "no answer found"로 테스트가 실패한다.
        // 그러니 스텁이 매칭됐다는 것 자체가 곧 검증(assertion) 역할을 한다.
        coEvery { api.getMealsByRange("2026-07-01", "2026-07-07", null) } returns emptyList()

        val result = repository.getMealsByRange(start, end, facilityId = null)

        assertEquals(emptyList<Any>(), result)
    }
}
