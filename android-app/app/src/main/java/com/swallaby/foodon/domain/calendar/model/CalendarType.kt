package com.swallaby.foodon.domain.calendar.model

import com.swallaby.foodon.domain.calendar.model.CalendarType.values

enum class CalendarType(val value: String) {
    MEAL("meal"),
    WEIGHT("weight"),
    RECOMMENDATION("recommendation");

    companion object {
        fun fromTabIndex(index: Int): CalendarType {
            return values().getOrElse(index) { MEAL }
        }
    }
}
