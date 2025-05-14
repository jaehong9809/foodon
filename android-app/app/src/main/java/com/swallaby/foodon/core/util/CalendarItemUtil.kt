package com.swallaby.foodon.core.util


import com.swallaby.foodon.domain.calendar.model.CalendarItem
import org.threeten.bp.LocalDate

fun List<CalendarItem>.toCalendarItemMap(): Map<String, CalendarItem> {
    return associateBy {
        when (it) {
            is CalendarItem.Meal -> it.data.date
            is CalendarItem.Weight -> it.data.date
            is CalendarItem.Recommendation -> it.data.date
        }
    }
}
