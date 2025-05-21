package com.swallaby.foodon.domain.calendar.model

sealed class CalendarItem {
    data class Meal(val data: CalendarMeal) : CalendarItem()
    data class Weight(val data: CalendarWeight) : CalendarItem()
    data class Recommendation(val data: CalendarRecommendation) : CalendarItem()
}
