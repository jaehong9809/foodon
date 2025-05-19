package com.swallaby.foodon.core.util

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import org.threeten.bp.DayOfWeek
import org.threeten.bp.LocalDate
import org.threeten.bp.LocalDateTime
import org.threeten.bp.YearMonth
import org.threeten.bp.format.DateTimeFormatter
import org.threeten.bp.temporal.WeekFields

object DateUtil {
    @Composable
    fun rememberWeekCount(currentYearMonth: YearMonth, today: LocalDate): Int {
        return remember(currentYearMonth, today) {
            // 오늘 날짜 기준으로 마지막 날짜 계산
            val lastDayOfMonth = currentYearMonth.atEndOfMonth()

            val weekFields = WeekFields.of(DayOfWeek.MONDAY, 1)
            val lastWeek = lastDayOfMonth.get(weekFields.weekOfMonth())

            // 현재 달이면 오늘 기준 주차만 반환, 다음 달이면 0 반환
            if (currentYearMonth.year == today.year && currentYearMonth.month == today.month) {
                today.get(weekFields.weekOfMonth())
            } else if (currentYearMonth.isBefore(YearMonth.from(today))) {
                lastWeek
            } else {
                0
            }
        }
    }

    fun formatDate(localDate: LocalDate): String {
        val outputFormatter = DateTimeFormatter.ofPattern("M월 d일")
        return localDate.format(outputFormatter)
    }

    fun formatTimeToHHmm(dateTime: LocalDateTime): String {
        val formatter = DateTimeFormatter.ofPattern("HH:mm")
        return dateTime.format(formatter)
    }

    fun getWeekOfMonth(date: LocalDate): Int {
        val weekFields = WeekFields.of(DayOfWeek.SUNDAY, 1)
        return date.get(weekFields.weekOfMonth())
    }

    // 각 주에 있는 날짜의 인덱스 반환
    fun getWeekDateRange(
        weekIndex: Int,
        yearMonth: YearMonth,
        firstDayOfWeek: Int
    ): Pair<Int, Int> {
        val daysInMonth = yearMonth.lengthOfMonth()
        val firstDayAbsoluteIndex = weekIndex * 7

        val validIndices = (0..6).filter { i ->
            val globalDayIndex = firstDayAbsoluteIndex + i
            val day = globalDayIndex - firstDayOfWeek + 1
            day in 1..daysInMonth
        }

        return if (validIndices.isNotEmpty()) {
            validIndices.first() to validIndices.last()
        } else {
            0 to 6
        }
    }

}
