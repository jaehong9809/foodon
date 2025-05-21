package com.swallaby.foodon.presentation.calendar.component

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.swallaby.foodon.core.ui.theme.WB500
import com.swallaby.foodon.core.util.DateUtil.getDateShape
import com.swallaby.foodon.domain.calendar.model.CalendarItem
import com.swallaby.foodon.domain.calendar.model.CalendarType
import com.swallaby.foodon.presentation.calendar.model.CalendarStatus
import org.threeten.bp.LocalDate

@Composable
fun CalendarBody(
    calendarItemMap: Map<String, CalendarItem>,
    calendarStatus: CalendarStatus,
    onDateSelected: (LocalDate) -> Unit
) {
    val calendarType = CalendarType.values()[calendarStatus.selectedTabIndex]
    val yearMonth = calendarStatus.currentYearMonth

    val firstDayOfMonth = yearMonth.atDay(1)
    val firstDayOfWeek = firstDayOfMonth.dayOfWeek.value % 7
    val daysInMonth = yearMonth.lengthOfMonth()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
    ) {
        var dayCounter = 1

        repeat(6) { weekIndex ->
            val isSelectedWeek = weekIndex == calendarStatus.selectedWeekIndex

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                for (dayOfWeek in 0..6) {
                    val day = if (weekIndex == 0 && dayOfWeek < firstDayOfWeek) null else dayCounter

                    if (day != null && day <= daysInMonth) {
                        val date = yearMonth.atDay(day)
                        val calendarItem = calendarItemMap[date.toString()]
                        val shape = getDateShape(
                            dayOfWeek = date.dayOfWeek.value % 7,
                            day = day,
                            daysInMonth = daysInMonth,
                            isSelectedWeek = isSelectedWeek
                        )

                        CalendarDateCell(
                            modifier = Modifier.weight(1f),
                            date = date,
                            calendarItem = calendarItem,
                            calendarType = calendarType,
                            shape = shape,
                            isSelectedWeek = isSelectedWeek,
                            isSelectedDate = calendarStatus.selectedDate == date,
                            today = calendarStatus.today,
                            onClick = onDateSelected
                        )

                        dayCounter++
                    } else {
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
            }
        }
    }
}

@Composable
private fun CalendarDateCell(
    modifier: Modifier,
    date: LocalDate,
    calendarItem: CalendarItem?,
    calendarType: CalendarType,
    shape: RoundedCornerShape,
    isSelectedWeek: Boolean,
    isSelectedDate: Boolean,
    today: LocalDate,
    onClick: (LocalDate) -> Unit
) {
    Box(modifier = modifier) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = if (calendarType == CalendarType.MEAL) 82.dp else 68.dp),
            contentAlignment = Alignment.TopCenter
        ) {
            CalendarWeekBackground(
                calendarType = calendarType,
                isSelectedWeek = isSelectedWeek,
                shape = shape
            )

            Box(
                modifier = if (calendarType == CalendarType.RECOMMENDATION) {
                    Modifier.height(41.dp)
                } else {
                    Modifier
                },
                contentAlignment = Alignment.Center
            ) {
                CalendarDayItem(
                    calendarType = calendarType,
                    calendarItem = calendarItem,
                    date = date,
                    today = today,
                    isSelected = isSelectedDate,
                    onClick = { onClick(date) }
                )
            }
        }
    }
}

@Composable
fun CalendarWeekBackground(
    calendarType: CalendarType,
    isSelectedWeek: Boolean,
    shape: RoundedCornerShape
) {
    val targetAlpha = if (calendarType == CalendarType.RECOMMENDATION && isSelectedWeek) 0.1f else 0f
    val animatedAlpha by animateFloatAsState(
        targetValue = targetAlpha,
        animationSpec = tween(durationMillis = 300),
        label = "WeekBackgroundAlpha"
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(41.dp)
            .clip(shape)
            .background(WB500.copy(alpha = animatedAlpha))
    )
}


