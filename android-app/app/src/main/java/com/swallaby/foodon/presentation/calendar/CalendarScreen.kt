package com.swallaby.foodon.presentation.calendar

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.swallaby.foodon.R
import com.swallaby.foodon.core.result.ResultState
import com.swallaby.foodon.core.ui.component.LoadingProgress
import com.swallaby.foodon.core.ui.component.MonthlyTabBar
import com.swallaby.foodon.core.ui.theme.Border025
import com.swallaby.foodon.core.ui.theme.FoodonTheme
import com.swallaby.foodon.core.ui.theme.G700
import com.swallaby.foodon.core.ui.theme.font.NotoTypography
import com.swallaby.foodon.core.util.DateUtil.rememberWeeksInMonth
import com.swallaby.foodon.domain.calendar.model.CalendarItem
import com.swallaby.foodon.domain.calendar.model.CalendarType
import com.swallaby.foodon.presentation.calendar.component.CalendarBody
import com.swallaby.foodon.presentation.calendar.component.CalendarHeader
import com.swallaby.foodon.presentation.calendar.component.TabContentPager
import com.swallaby.foodon.presentation.calendar.component.WeeklyLabel
import com.swallaby.foodon.presentation.calendar.component.WeightBox
import com.swallaby.foodon.presentation.calendar.viewmodel.CalendarViewModel
import kotlinx.coroutines.launch
import org.threeten.bp.LocalDate
import org.threeten.bp.YearMonth

@Composable
fun CalendarScreen(
    viewModel: CalendarViewModel = viewModel(),
) {

    val today = remember { LocalDate.now() }
    val baseYearMonth = remember { YearMonth.from(today) }

    val monthOffsetRange = -12..12

    val pagerState = rememberPagerState(initialPage = 12, pageCount = { monthOffsetRange.count() })
    val scope = rememberCoroutineScope()

    var selectedTabIndex by remember { mutableIntStateOf(0) }
    val calendarType = CalendarType.values()[selectedTabIndex]

    var currentYearMonth by remember { mutableStateOf(baseYearMonth.plusMonths((pagerState.currentPage - 12).toLong())) }

    val uiState by viewModel.uiState.collectAsState()
    val selectedDate = uiState.selectedDate

    val calendarItems = (uiState.calendarState as? ResultState.Success)?.data.orEmpty()

    val calendarItemMap = calendarItems.associateBy {
        when (it) {
            is CalendarItem.Meal -> it.data.date
            is CalendarItem.Weight -> it.data.date
            is CalendarItem.Recommendation -> it.data.date
        }
    }

    val selectedItem = calendarItemMap[selectedDate.toString()]

    val weeksInCurrentMonth = rememberWeeksInMonth(currentYearMonth, today)

    // TODO: 맨 처음 API 두 번 호출되는 문제 수정 필요

    // 페이지가 변경될 때 데이터 갱신
    LaunchedEffect(pagerState.currentPage) {
        currentYearMonth = baseYearMonth.plusMonths((pagerState.currentPage - 12).toLong())
        viewModel.fetchCalendarData(calendarType, currentYearMonth.toString())
    }

    // 탭이 변경될 때 데이터 갱신
    LaunchedEffect(selectedTabIndex) {
        Log.d("CalendarScreen", calendarType.value)

        viewModel.fetchCalendarData(calendarType, currentYearMonth.toString())

        if (calendarType == CalendarType.WEIGHT) {
            viewModel.fetchUserWeight()
        } else if (calendarType == CalendarType.RECOMMENDATION) {
            viewModel.fetchRecommendFoods(
                yearMonth = currentYearMonth.toString()
            )
        }
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
                        scope.launch {
                            pagerState.animateScrollToPage(pagerState.currentPage - 1)
                        }
                    },
                    onNextMonth = {
                        scope.launch {
                            pagerState.animateScrollToPage(pagerState.currentPage + 1)
                        }
                    }
                )

                WeeklyLabel()

                HorizontalPager(
                    state = pagerState,
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                ) { page ->
                    val yearMonth = baseYearMonth.plusMonths((page - 12).toLong())

                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 8.dp),
                        contentAlignment = Alignment.TopStart
                    ) {
                        CalendarBody(
                            calendarItemMap = calendarItemMap,
                            type = calendarType,
                            yearMonth = yearMonth,
                            selectedDate = selectedDate,
                            today = today,
                            onDateSelected = { viewModel.selectDate(it) }
                        )

                        if (uiState.calendarState is ResultState.Loading) {
                            LoadingProgress()
                        }

                    }

                }

                Spacer(modifier = Modifier.height(8.dp))

                Box(
                    modifier = Modifier.padding(horizontal = 24.dp)
                ) {
                    if (calendarType == CalendarType.MEAL) {
                        Text(
                            text = stringResource(R.string.tab_meal_bottom),
                            style = NotoTypography.NotoMedium13,
                            color = G700
                        )
                    } else if (calendarType == CalendarType.WEIGHT) {
                        WeightBox(
                            text = stringResource(R.string.tab_weight_bottom)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(1.dp)
                        .background(Border025)
                )

                TabContentPager(
                    selectedTab = selectedTabIndex,
                    selectedItem = selectedItem,
                    userWeight = uiState.weightState,
                    recommendFoods = uiState.recommendFoods,
                    weeksInCurrentMonth = weeksInCurrentMonth,
                    onTabChanged = {
                        selectedTabIndex = it
                    },
                    onWeeklyTabChanged = {
                        viewModel.fetchRecommendFoods(
                            yearMonth = currentYearMonth.toString(),
                            week = (it + 1)
                        )
                    }
                )

            }
        }
    )
}

@Preview(showBackground = true)
@Composable
fun CalendarPreview() {
    FoodonTheme {
        CalendarScreen()
    }
}