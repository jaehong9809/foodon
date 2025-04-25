package com.swallaby.foodon.domain.calendar.usecase

import com.swallaby.foodon.core.result.ApiResult
import com.swallaby.foodon.domain.calendar.model.RecommendFood
import com.swallaby.foodon.domain.calendar.repository.CalendarRepository
import javax.inject.Inject

class GetRecommendFoodUseCase @Inject constructor(
    private val repository: CalendarRepository
) {
    suspend operator fun invoke(yearMonth: String, week: Int, day: String): ApiResult<List<RecommendFood>> {
        return repository.getRecommendFood(yearMonth, week, day)
    }
}