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
import org.threeten.bp.YearMonth
import javax.inject.Inject

class CalendarRepositoryImpl @Inject constructor(
    private val api: CalendarApi
): CalendarRepository {

    override suspend fun getCalendarMeals(date: YearMonth): ApiResult<List<CalendarMeal>> = safeApiCall {
        api.getCalendarMeals(date.toString()).getOrThrow { it.map { data -> data.toDomain() } }
    }

    override suspend fun getCalendarWeights(date: YearMonth): ApiResult<List<CalendarWeight>> = safeApiCall {
        api.getCalendarWeights(date.toString()).getOrThrow { it.map { data -> data.toDomain() } }
    }

    override suspend fun getCalendarRecommendations(date: YearMonth): ApiResult<List<CalendarRecommendation>> = safeApiCall {
        api.getCalendarRecommendations(date.toString()).getOrThrow { it.map { data -> data.toDomain() } }
    }

    override suspend fun getUserWeight(): ApiResult<UserWeight> = safeApiCall {
        api.getUserWeight().getOrThrow { it.toDomain() }
    }

    override suspend fun getRecommendFood(
        yearMonth: YearMonth,
        week: Int?,
    ): ApiResult<List<RecommendFood>> = safeApiCall {
        api.getRecommendFoods(yearMonth.toString(), week).getOrThrow { it.map { data -> data.toDomain() } }
    }

}