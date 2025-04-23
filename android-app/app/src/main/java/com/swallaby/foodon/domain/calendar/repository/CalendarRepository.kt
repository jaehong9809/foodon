package com.swallaby.foodon.domain.calendar.repository

import com.swallaby.foodon.core.result.ApiResult
import com.swallaby.foodon.domain.calendar.model.Meal
import com.swallaby.foodon.domain.calendar.model.Recommendation
import com.swallaby.foodon.domain.calendar.model.Weight

interface CalendarRepository {
    suspend fun getCalendarMeals(date: String): ApiResult<List<Meal>>

    suspend fun getCalendarWeights(date: String): ApiResult<List<Weight>>

    suspend fun getCalendarRecommendations(date: String): ApiResult<List<Recommendation>>
}