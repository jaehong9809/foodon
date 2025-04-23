package com.swallaby.foodon.data.calendar.dto

import com.swallaby.foodon.domain.calendar.model.Recommendation

data class RecommendationResponse(
    val calendarId: Long = 0,
    val date: String = "",
    val thumbnailImage: String = ""
)

fun RecommendationResponse.toDomain(): Recommendation {
    return Recommendation(
        calendarId = this.calendarId,
        date = this.date,
        thumbnailImage = this.thumbnailImage
    )
}
