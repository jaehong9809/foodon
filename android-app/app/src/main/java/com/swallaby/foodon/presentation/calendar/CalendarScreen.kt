package com.swallaby.foodon.presentation.calendar

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.swallaby.foodon.core.ui.component.MonthlyTabBar
import com.swallaby.foodon.core.ui.theme.Border025
import com.swallaby.foodon.core.ui.theme.FoodonTheme
import com.swallaby.foodon.domain.calendar.model.CalendarType
import com.swallaby.foodon.presentation.calendar.component.CalendarBody
import com.swallaby.foodon.presentation.calendar.component.CalendarHeader
import com.swallaby.foodon.presentation.calendar.component.TabContentPager
import com.swallaby.foodon.presentation.calendar.component.WeeklyLabel
import com.swallaby.foodon.presentation.calendar.viewmodel.CalendarViewModel
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.YearMonth

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CalendarScreen(
    viewModel: CalendarViewModel = viewModel(),
) {
//    var selectedTabIndex by remember { mutableIntStateOf(0) }
//    val calendarType = CalendarType.values()[selectedTabIndex]
//
//    // 탭 변경 시 캘린더 데이터 갱신
//    LaunchedEffect(calendarType) {
//        viewModel.fetchCalendarData(calendarType, "2025-04")
//    }

    val monthOffsetRange = -12..12
    val today = remember { LocalDate.now() }
    val baseYearMonth = remember { YearMonth.from(today) }

    val pagerState = rememberPagerState(initialPage = 12, pageCount = {monthOffsetRange.count()}) // 0부터 시작하니까 base는 중간
    val coroutineScope = rememberCoroutineScope()

    var selectedTabIndex by remember { mutableIntStateOf(0) }
    val calendarType = CalendarType.values()[selectedTabIndex]

    val currentYearMonth = remember(pagerState.currentPage) {
        baseYearMonth.plusMonths((pagerState.currentPage - 12).toLong())
    }

    val selectedDate by viewModel.selectedDate
    val calorieMap by viewModel.calorieDataMap

    LaunchedEffect(currentYearMonth, calendarType) {
        viewModel.fetchCalendarData(calendarType, currentYearMonth.toString())
    }

    Scaffold(
        floatingActionButton = {
            MonthlyTabBar(
                modifier = Modifier.padding(bottom = 16.dp),
                selectedIndex = selectedTabIndex,
                onTabSelected = { selectedTabIndex = it }
            )
        },
        floatingActionButtonPosition = FabPosition.Center,
        content = { innerPadding ->
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .verticalScroll(rememberScrollState())
                    .padding(bottom = 96.dp)
                    .fillMaxSize(),
            ) {
                CalendarHeader(
                    currentYearMonth = currentYearMonth,
                    onPreviousMonth = {
                        coroutineScope.launch {
                            pagerState.animateScrollToPage(pagerState.currentPage - 1)
                        }
                    },
                    onNextMonth = {
                        coroutineScope.launch {
                            pagerState.animateScrollToPage(pagerState.currentPage + 1)
                        }
                    }
                )

                WeeklyLabel()

                HorizontalPager(
                    state = pagerState,
                    modifier = Modifier.fillMaxWidth().weight(1f)
                ) { page ->
                    val yearMonth = baseYearMonth.plusMonths((page - 12).toLong())

                    CalendarBody(
                        yearMonth = yearMonth,
                        calorieDataMap = calorieMap,
                        selectedDate = selectedDate,
                        onDateSelected = { viewModel.selectDate(it) }
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(1.dp)
                        .background(Border025)
                )

                TabContentPager(
                    selectedTab = selectedTabIndex,
                    onTabChanged = {
                        selectedTabIndex = it
                    }
                )
            }
        }
    )
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true)
@Composable
fun CalendarPreview() {
    FoodonTheme {
        CalendarScreen()
    }
}