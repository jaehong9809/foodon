package com.swallaby.foodon.data.calendar.remote.dto

import com.swallaby.foodon.domain.calendar.model.CalendarMeal

data class MealResponse(
    val intakeLogId: Long = 0,
    val date: String = "",
    val intakeKcal: Int = 0,
    val goalKcal: Int = 0
)

fun MealResponse.toDomain(): CalendarMeal {
    return CalendarMeal(
        intakeLogId = this.intakeLogId,
        date = this.date,
        intakeKcal = this.intakeKcal,
        goalKcal = this.goalKcal
    )
}
