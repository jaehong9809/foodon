package com.swallaby.foodon.core.util

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.unit.dp
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

    @Composable
    fun getDateShape(
        dayOfWeekFromDate: Int,
        day: Int,
        daysInMonth: Int,
        isSelectedWeek: Boolean,
    ): RoundedCornerShape {
        val targetTopStart = when {
            isSelectedWeek && (day == 1 && dayOfWeekFromDate == 6) || (day == daysInMonth && dayOfWeekFromDate == 0) -> 100.dp // 하루만 있는 경우
            isSelectedWeek && (dayOfWeekFromDate == 0 || day == 1) -> 100.dp // Sunday 또는 월 첫째날
            else -> 0.dp
        }

        val targetBottomStart = when {
            isSelectedWeek && (day == 1 && dayOfWeekFromDate == 6) || (day == daysInMonth && dayOfWeekFromDate == 0) -> 100.dp // 하루만 있는 경우
            isSelectedWeek && (dayOfWeekFromDate == 0 || day == 1) -> 100.dp // Sunday 또는 월 첫째날
            else -> 0.dp
        }

        val targetTopEnd = when {
            isSelectedWeek && (day == daysInMonth && dayOfWeekFromDate == 0) || (dayOfWeekFromDate == 6 || day == daysInMonth) -> 100.dp // Saturday 또는 월 마지막날
            else -> 0.dp
        }

        val targetBottomEnd = when {
            isSelectedWeek && (day == daysInMonth && dayOfWeekFromDate == 0) || (dayOfWeekFromDate == 6 || day == daysInMonth) -> 100.dp // Saturday 또는 월 마지막날
            else -> 0.dp
        }

        val topStart by animateDpAsState(targetTopStart)
        val bottomStart by animateDpAsState(targetBottomStart)
        val topEnd by animateDpAsState(targetTopEnd)
        val bottomEnd by animateDpAsState(targetBottomEnd)

        return RoundedCornerShape(
            topStart = topStart,
            bottomStart = bottomStart,
            topEnd = topEnd,
            bottomEnd = bottomEnd
        )
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
        val firstDayOfMonth = date.withDayOfMonth(1)
        val firstDayWeekday = firstDayOfMonth.dayOfWeek.value.let { if (it == 7) 0 else it }

        val dayOffset = firstDayWeekday + date.dayOfMonth - 1
        return dayOffset / 7
    }

}
