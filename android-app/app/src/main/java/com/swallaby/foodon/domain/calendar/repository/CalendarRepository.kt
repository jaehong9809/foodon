package com.swallaby.foodon.domain.calendar.repository

import com.swallaby.foodon.core.result.ApiResult
import com.swallaby.foodon.domain.calendar.model.CalendarMeal
import com.swallaby.foodon.domain.calendar.model.CalendarRecommendation
import com.swallaby.foodon.domain.calendar.model.CalendarWeight

interface CalendarRepository {
    suspend fun getCalendarMeals(date: String): ApiResult<List<CalendarMeal>>

    suspend fun getCalendarWeights(date: String): ApiResult<List<CalendarWeight>>

    suspend fun getCalendarRecommendations(date: String): ApiResult<List<CalendarRecommendation>>
}