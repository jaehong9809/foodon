package com.swallaby.foodon.data.calendar.remote.dto

import com.swallaby.foodon.domain.calendar.model.CalendarRecommendation

data class RecommendationResponse(
    val mealId: Long = 0,
    val date: String = "",
    val thumbnailImage: String = ""
)

fun RecommendationResponse.toDomain(): CalendarRecommendation {
    return CalendarRecommendation(
        mealId = this.mealId,
        date = this.date,
        thumbnailImage = this.thumbnailImage
    )
}
