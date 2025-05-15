package com.swallaby.foodon.presentation.main.model

import org.threeten.bp.LocalDate
import org.threeten.bp.YearMonth

data class CalendarInfo(
    val today: LocalDate,
    val selectedDate: LocalDate,
    val currentYearMonth: YearMonth,
    val currentWeekStart: LocalDate
)
