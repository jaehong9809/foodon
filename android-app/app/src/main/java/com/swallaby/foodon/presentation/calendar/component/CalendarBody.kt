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
import androidx.compose.foundation.layout.padding
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
                            // 배경 Box
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .align(Alignment.TopCenter)
                                    .background(
                                        color = if (type == CalendarType.RECOMMENDATION && isSelectedWeek)
                                            WB500.copy(alpha = 0.1f)
                                        else Color.Transparent,
                                        shape = shape
                                    )
                            ) {
                                // 내부 Box는 padding을 추가해서 내용이 위로 밀리도록 설정
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(41.dp)  // 높이 설정
                                        .align(Alignment.Center)  // 중앙 정렬
                                        .padding(top = 8.dp)  // 위쪽 패딩 추가
                                )
                            }

                            // 날짜 Box
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .heightIn(min = 82.dp),
                                contentAlignment = Alignment.TopCenter
                            ) {
//                                CalendarDayItem(
//                                    calendarItem = calendarItem,
//                                    type = type,
//                                    date = date,
//                                    today = today,
//                                    isSelected = selectedDate == date,
//                                    onClick = { onDateSelected(date) }
//                                )

                                Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
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

