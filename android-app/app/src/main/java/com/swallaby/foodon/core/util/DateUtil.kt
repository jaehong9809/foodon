package com.swallaby.foodon.core.util

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
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
        dayOfWeek: Int,
        day: Int,
        daysInMonth: Int,
        isSelectedWeek: Boolean,
    ): RoundedCornerShape {

        val isFirstDay = day == 1
        val isLastDay = day == daysInMonth
        val isSunday = dayOfWeek == 0
        val isSaturday = dayOfWeek == 6

        val topStart = when {
            !isSelectedWeek -> 0.dp
            (isFirstDay && isSaturday) || (isLastDay && isSunday) -> 100.dp
            isFirstDay || isSunday -> 100.dp
            else -> 0.dp
        }

        val topEnd = when {
            !isSelectedWeek -> 0.dp
            (isFirstDay && isSaturday) || (isLastDay && isSunday) -> 100.dp
            isLastDay || isSaturday -> 100.dp
            else -> 0.dp
        }

        val animatedTopStart by animateDpAsState(
            targetValue = topStart,
            animationSpec = tween(durationMillis = 300),
            label = "topStart"
        )
        val animatedTopEnd by animateDpAsState(
            targetValue = topEnd,
            animationSpec = tween(durationMillis = 300),
            label = "topEnd"
        )

        return RoundedCornerShape(
            topStart = animatedTopStart,
            topEnd = animatedTopEnd,
            bottomEnd = animatedTopEnd,
            bottomStart = animatedTopStart
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
        val weekFields = WeekFields.of(DayOfWeek.SUNDAY, 1)
        return date.get(weekFields.weekOfMonth())
    }

}
