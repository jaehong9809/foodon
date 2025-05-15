package com.swallaby.foodon.data.calendar.remote.dto

import com.swallaby.foodon.domain.calendar.model.CalendarRecommendation
import com.swallaby.foodon.domain.calendar.model.MealThumbnailInfo

data class RecommendationResponse(
    val date: String = "",
    val meals: List<MealThumbnailInfo> = emptyList()
)

fun RecommendationResponse.toDomain(): CalendarRecommendation {
    return CalendarRecommendation(
        date = this.date,
        meals = this.meals
    )
}
