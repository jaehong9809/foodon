package com.swallaby.foodon.presentation.calendar.component

import android.util.Log
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
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.swallaby.foodon.core.ui.theme.WB500
import com.swallaby.foodon.core.util.DateUtil.getDateShape
import com.swallaby.foodon.domain.calendar.model.CalendarItem
import com.swallaby.foodon.domain.calendar.model.CalendarType
import org.threeten.bp.LocalDate
import org.threeten.bp.YearMonth

@Composable
fun CalendarBody(
    calendarItemMap: Map<String, CalendarItem>,
    type: CalendarType = CalendarType.MEAL,
    yearMonth: YearMonth,
    selectedDate: LocalDate?,
    today: LocalDate,
    selectedWeekIndex: Int,
    onDateSelected: (LocalDate) -> Unit
) {
    val firstDayOfMonth = yearMonth.atDay(1)
    val firstDayOfWeek = firstDayOfMonth.dayOfWeek.value % 7
    val daysInMonth = yearMonth.lengthOfMonth()

    Log.d("CalendarBody", "$selectedWeekIndex")

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
    ) {
        var dayCounter = 1

        for (week in 0 until 6) {
            val isSelectedWeek = week == selectedWeekIndex

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                for (dayOfWeek in 0..6) {
                    val day = if (week == 0 && dayOfWeek < firstDayOfWeek) {
                        null  // 첫 번째 주의 첫날 이전은 비어있음
                    } else {
                        dayCounter
                    }

                    if (day != null && day <= daysInMonth) {
                        val date = yearMonth.atDay(day)
                        val calendarItem = calendarItemMap[date.toString()]

                        val dayOfWeekFromDate = date.dayOfWeek.value % 7
                        val shape = getDateShape(dayOfWeekFromDate, day, daysInMonth, isSelectedWeek)

                        Box(
                            modifier = Modifier
                                .weight(1f)
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .heightIn(min = 82.dp),  // 날짜 영역만큼 높이를 지정
                                contentAlignment = Alignment.TopCenter
                            ) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(41.dp)  // 배경 높이를 41.dp로 설정
                                        .background(
                                            color = if (type == CalendarType.RECOMMENDATION && isSelectedWeek)
                                                WB500.copy(alpha = 0.1f)
                                            else Color.Transparent,
                                            shape = shape
                                        )
                                )

                                Box(
                                    modifier = Modifier
                                        .then(
                                            if (type == CalendarType.RECOMMENDATION && isSelectedWeek) {
                                                Modifier.height(41.dp) // 조건에 맞으면 41.dp 높이를 주기
                                            } else {
                                                Modifier // 그렇지 않으면 높이를 지정하지 않음
                                            }
                                        ),
                                    contentAlignment = Alignment.Center // 내용물을 중앙에 배치
                                ) {
                                    CalendarDayItem(
                                        calendarItem = calendarItem,
                                        type = type,
                                        date = date,
                                        today = today,
                                        isSelected = selectedDate == date,
                                        onClick = { onDateSelected(date) }
                                    )
                                }
                            }

                        }

                        dayCounter++
                    } else {
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
            }
        }
    }
}

