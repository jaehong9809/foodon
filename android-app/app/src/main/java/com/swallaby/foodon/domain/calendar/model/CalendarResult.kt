package com.swallaby.foodon.domain.calendar.model

sealed class CalendarResult {
    data class Meals(val data: List<Meal>) : CalendarResult()
    data class Weights(val data: List<Weight>) : CalendarResult()
    data class Recommendations(val data: List<Recommendation>) : CalendarResult()
}