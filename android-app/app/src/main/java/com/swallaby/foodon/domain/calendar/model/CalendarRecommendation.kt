package com.swallaby.foodon.domain.calendar.model

data class CalendarRecommendation(
    override val calendarType: CalendarType = CalendarType.RECOMMENDATION,
    override val date: String = "",
    val meals: List<MealThumbnailInfo> = emptyList()
) : BaseCalendar
