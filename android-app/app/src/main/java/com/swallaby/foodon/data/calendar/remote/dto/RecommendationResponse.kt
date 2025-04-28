package com.swallaby.foodon.data.calendar.remote.dto

import com.swallaby.foodon.domain.calendar.model.CalendarRecommendation

data class RecommendationResponse(
    val calendarId: Long = 0,
    val date: String = "",
    val thumbnailImage: String = ""
)

fun RecommendationResponse.toDomain(): CalendarRecommendation {
    return CalendarRecommendation(
        calendarId = this.calendarId,
        date = this.date,
        thumbnailImage = this.thumbnailImage
    )
}
