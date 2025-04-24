package com.swallaby.foodon.domain.calendar.model

data class CalendarWeight(
    override val calendarType: CalendarType = CalendarType.WEIGHT,
    override val calendarId: Long = 0,
    override val date: String = "",
    val weight: Int = 0
) : BaseCalendar
