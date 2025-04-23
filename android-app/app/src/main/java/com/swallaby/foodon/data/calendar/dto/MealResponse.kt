package com.swallaby.foodon.data.calendar.dto

import com.swallaby.foodon.domain.calendar.model.Meal

data class MealResponse(
    val calendarId: Long = 0,
    val date: String = "",
    val intakeKcal: Int = 0,
    val goalKcal: Int = 0
)

fun MealResponse.toDomain(): Meal {
    return Meal(
        calendarId = this.calendarId,
        date = this.date,
        intakeKcal = this.intakeKcal,
        goalKcal = this.goalKcal
    )
}
