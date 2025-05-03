package com.swallaby.foodon.core.util

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.unit.dp
import org.threeten.bp.DayOfWeek
import org.threeten.bp.LocalDate
import org.threeten.bp.YearMonth
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

    @Composable
    fun getDateShape(dayOfWeekFromDate: Int, day: Int, daysInMonth: Int, isSelectedWeek: Boolean): RoundedCornerShape {
        return if (isSelectedWeek) {
            when {
                (day == 1 && dayOfWeekFromDate == 6) || (day == daysInMonth && dayOfWeekFromDate == 0) -> {
                    RoundedCornerShape(100.dp) // 하루만 있는 경우
                }
                dayOfWeekFromDate == 0 || day == 1 -> { // Sunday 또는 월 첫째날
                    RoundedCornerShape(
                        topStart = 100.dp,
                        bottomStart = 100.dp,
                        topEnd = 0.dp,
                        bottomEnd = 0.dp
                    )
                }
                dayOfWeekFromDate == 6 || day == daysInMonth -> { // Saturday 또는 월 마지막날
                    RoundedCornerShape(
                        topStart = 0.dp,
                        bottomStart = 0.dp,
                        topEnd = 100.dp,
                        bottomEnd = 100.dp
                    )
                }
                else -> {
                    RoundedCornerShape(0.dp)
                }
            }
        } else {
            RoundedCornerShape(0.dp)
        }
    }

    fun calculateWeeksOfMonth(yearMonth: YearMonth): List<List<LocalDate?>> {
        val firstDayOfMonth = yearMonth.atDay(1)
        val lastDayOfMonth = yearMonth.atEndOfMonth()

        // 주의 시작일을 가장 가까운 이전 일요일로 조정
        val startOfWeek = firstDayOfMonth.minusDays((firstDayOfMonth.dayOfWeek.value % 7).toLong())

        val weeks = mutableListOf<List<LocalDate?>>()
        var current = startOfWeek

        while (current <= lastDayOfMonth || weeks.size < 6) {
            val week = (0..6).map {
                val date = current.plusDays(it.toLong())
                if (date.month == yearMonth.month) date else null
            }
            weeks.add(week)
            current = current.plusDays(7)
        }

        return weeks
    }


}
