package com.swallaby.foodon.domain.calendar.usecase

import com.swallaby.foodon.core.result.ApiResult
import com.swallaby.foodon.domain.calendar.model.CalendarType
import com.swallaby.foodon.domain.calendar.repository.CalendarRepository
import javax.inject.Inject

class GetCalendarUseCase @Inject constructor(
    private val repository: CalendarRepository
) {
    suspend operator fun invoke(type: CalendarType, date: String): ApiResult<List<Any>> {
        return when (type) {
            CalendarType.MEAL -> repository.getCalendarMeals(date)
            CalendarType.WEIGHT -> repository.getCalendarWeights(date)
            CalendarType.Recommendation -> repository.getCalendarRecommendations(date)
        }
    }
}
