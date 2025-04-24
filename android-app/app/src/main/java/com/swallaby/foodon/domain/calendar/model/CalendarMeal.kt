package com.swallaby.foodon.domain.calendar.model

data class CalendarMeal(
    override val calendarType: CalendarType = CalendarType.MEAL,
    override val calendarId: Long = 0,
    override val date: String = "",
    val intakeKcal: Int = 0,
    val goalKcal: Int = 0
) : BaseCalendar
