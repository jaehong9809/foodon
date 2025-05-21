package com.swallaby.foodon.data.calendar.remote.dto

import com.swallaby.foodon.domain.calendar.model.CalendarWeight

data class WeightResponse(
    val weightRecordId: Long = 0,
    val date: String = "",
    val weight: Int = 0
)

fun WeightResponse.toDomain(): CalendarWeight {
    return CalendarWeight(
        weightRecordId = this.weightRecordId,
        date = this.date,
        weight = this.weight
    )
}
