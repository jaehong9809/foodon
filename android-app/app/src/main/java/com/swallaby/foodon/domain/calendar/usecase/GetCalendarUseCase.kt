package com.swallaby.foodon.domain.calendar.usecase

import com.swallaby.foodon.core.result.ApiResult
import com.swallaby.foodon.core.result.map
import com.swallaby.foodon.domain.calendar.model.CalendarItem
import com.swallaby.foodon.domain.calendar.model.CalendarType
import com.swallaby.foodon.domain.calendar.repository.CalendarRepository
import org.threeten.bp.YearMonth
import javax.inject.Inject

class GetCalendarUseCase @Inject constructor(
    private val repository: CalendarRepository
) {
    suspend operator fun invoke(type: CalendarType, date: YearMonth): ApiResult<List<CalendarItem>> {
        return when (type) {
            CalendarType.MEAL -> repository.getCalendarMeals(date).map { meals ->
                meals.map { CalendarItem.Meal(it) }
            }
            CalendarType.WEIGHT -> repository.getCalendarWeights(date).map { weights ->
                weights.map { CalendarItem.Weight(it) }
            }
            CalendarType.RECOMMENDATION -> repository.getCalendarRecommendations(date).map { recs ->
                recs.map { CalendarItem.Recommendation(it) }
            }
        }
    }
}
