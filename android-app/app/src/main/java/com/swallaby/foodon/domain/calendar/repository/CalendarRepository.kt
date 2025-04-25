package com.swallaby.foodon.domain.calendar.repository

import com.swallaby.foodon.core.result.ApiResult
import com.swallaby.foodon.domain.calendar.model.CalendarMeal
import com.swallaby.foodon.domain.calendar.model.CalendarRecommendation
import com.swallaby.foodon.domain.calendar.model.CalendarWeight
import com.swallaby.foodon.domain.calendar.model.RecommendFood
import com.swallaby.foodon.domain.calendar.model.UserWeight

interface CalendarRepository {
    suspend fun getCalendarMeals(date: String): ApiResult<List<CalendarMeal>>

    suspend fun getCalendarWeights(date: String): ApiResult<List<CalendarWeight>>

    suspend fun getCalendarRecommendations(date: String): ApiResult<List<CalendarRecommendation>>

    suspend fun getUserWeight(): ApiResult<UserWeight>

    suspend fun getRecommendFood(yearMonth: String, week: Int?, day: String?): ApiResult<List<RecommendFood>>
}