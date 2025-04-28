package com.swallaby.foodon.domain.calendar.model

data class CalendarRecommendation(
    override val calendarType: CalendarType = CalendarType.RECOMMENDATION,
    override val date: String = "",
    val mealId: Long = 0,
    val thumbnailImage: String = "",
) : BaseCalendar
