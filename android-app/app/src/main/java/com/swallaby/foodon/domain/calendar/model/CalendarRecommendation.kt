package com.swallaby.foodon.domain.calendar.model

data class CalendarRecommendation(
    override val calendarType: CalendarType = CalendarType.RECOMMENDATION,
    override val calendarId: Long = 0,
    override val date: String = "",
    val thumbnailImage: String = "",
) : BaseCalendar
