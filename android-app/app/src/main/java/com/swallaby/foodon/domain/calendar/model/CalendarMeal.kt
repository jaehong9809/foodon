package com.swallaby.foodon.domain.calendar.model

data class CalendarMeal(
    override val calendarType: CalendarType = CalendarType.MEAL,
    override val date: String = "",
    val intakeLogId: Long = 0,
    val intakeKcal: Int = 0,
    val goalKcal: Int = 0
) : BaseCalendar
