package com.swallaby.foodon.presentation.calendar.model

import org.threeten.bp.LocalDate
import org.threeten.bp.YearMonth

data class CalendarStatus(
    val today: LocalDate,
    val selectedDate: LocalDate,
    val currentYearMonth: YearMonth,
    val selectedTabIndex: Int,
    val selectedWeekIndex: Int,
    val weekCount: Int
)