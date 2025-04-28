package com.swallaby.foodon.domain.calendar.model

interface  BaseCalendar {
    val calendarType: CalendarType
    val calendarId: Long
    val date: String
}
