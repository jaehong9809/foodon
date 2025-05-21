package com.swallaby.foodon.domain.calendar.repository

import com.swallaby.foodon.core.result.ApiResult
import com.swallaby.foodon.domain.calendar.model.CalendarMeal
import com.swallaby.foodon.domain.calendar.model.CalendarRecommendation
import com.swallaby.foodon.domain.calendar.model.CalendarWeight
import com.swallaby.foodon.domain.calendar.model.RecommendFood
import com.swallaby.foodon.domain.calendar.model.UserWeight
import org.threeten.bp.YearMonth

interface CalendarRepository {
    suspend fun getCalendarMeals(date: YearMonth): ApiResult<List<CalendarMeal>>

    suspend fun getCalendarWeights(date: YearMonth): ApiResult<List<CalendarWeight>>

    suspend fun getCalendarRecommendations(date: YearMonth): ApiResult<List<CalendarRecommendation>>

    suspend fun getUserWeight(): ApiResult<UserWeight>

    suspend fun getRecommendFood(yearMonth: YearMonth, week: Int?): ApiResult<List<RecommendFood>>

    suspend fun updateUserWeight(weight: Int): ApiResult<Unit>
}