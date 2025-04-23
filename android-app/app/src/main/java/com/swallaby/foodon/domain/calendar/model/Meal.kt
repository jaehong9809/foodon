package com.swallaby.foodon.domain.calendar.model

data class Meal(
    val calendarId: Long = 0,
    val date: String = "",
    val intakeKcal: Int = 0,
    val goalKcal: Int = 0
)
