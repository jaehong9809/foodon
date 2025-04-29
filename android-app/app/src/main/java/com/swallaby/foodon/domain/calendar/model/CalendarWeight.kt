package com.swallaby.foodon.domain.calendar.model

data class CalendarWeight(
    override val calendarType: CalendarType = CalendarType.WEIGHT,
    override val date: String = "",
    val weightRecordId: Long = 0,
    val weight: Int = 0
) : BaseCalendar
