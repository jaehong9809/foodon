package com.swallaby.foodon.data.calendar.remote.repository

import com.swallaby.foodon.core.data.remote.getOrThrow
import com.swallaby.foodon.core.result.ApiResult
import com.swallaby.foodon.core.result.safeApiCall
import com.swallaby.foodon.data.calendar.remote.api.CalendarApi
import com.swallaby.foodon.data.calendar.remote.dto.toDomain
import com.swallaby.foodon.domain.calendar.model.CalendarMeal
import com.swallaby.foodon.domain.calendar.model.CalendarRecommendation
import com.swallaby.foodon.domain.calendar.model.CalendarWeight
import com.swallaby.foodon.domain.calendar.model.RecommendFood
import com.swallaby.foodon.domain.calendar.model.UserWeight
import com.swallaby.foodon.domain.calendar.repository.CalendarRepository
import javax.inject.Inject

class CalendarRepositoryImpl @Inject constructor(
    private val api: CalendarApi
): CalendarRepository {

    override suspend fun getCalendarMeals(date: String): ApiResult<List<CalendarMeal>> = safeApiCall {
        api.getCalendarMeals(date).getOrThrow { it.map { data -> data.toDomain() } }
    }

    override suspend fun getCalendarWeights(date: String): ApiResult<List<CalendarWeight>> = safeApiCall {
        api.getCalendarWeights(date).getOrThrow { it.map { data -> data.toDomain() } }
    }

    override suspend fun getCalendarRecommendations(date: String): ApiResult<List<CalendarRecommendation>> = safeApiCall {
        api.getCalendarRecommendations(date).getOrThrow { it.map { data -> data.toDomain() } }
    }

    override suspend fun getUserWeight(): ApiResult<UserWeight> = safeApiCall {
        api.getUserWeight().getOrThrow { it.toDomain() }
    }

    override suspend fun getRecommendFood(
        yearMonth: String,
        week: Int,
        day: String
    ): ApiResult<List<RecommendFood>> = safeApiCall {
        api.getRecommendFoods(yearMonth, week, day).getOrThrow { it.map { data -> data.toDomain() } }
    }

}