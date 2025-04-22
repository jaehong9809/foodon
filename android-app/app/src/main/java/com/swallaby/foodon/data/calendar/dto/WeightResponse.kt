package com.swallaby.foodon.data.calendar.dto

import com.swallaby.foodon.domain.calendar.model.Weight

data class WeightResponse(
    val calendarId: Long = 0,
    val date: String = "",
    val weight: Int = 0
)

fun WeightResponse.toDomain(): Weight {
    return Weight(
        calendarId = this.calendarId,
        date = this.date,
        weight = this.weight
    )
}
