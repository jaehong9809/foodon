package com.swallaby.foodon.presentation.calendar

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.FabPosition
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.swallaby.foodon.R
import com.swallaby.foodon.core.result.ResultState
import com.swallaby.foodon.core.ui.component.MonthlyTabBar
import com.swallaby.foodon.core.ui.theme.Border025
import com.swallaby.foodon.core.ui.theme.FoodonTheme
import com.swallaby.foodon.core.ui.theme.G700
import com.swallaby.foodon.core.ui.theme.font.NotoTypography
import com.swallaby.foodon.core.util.DateUtil.getWeekOfMonth
import com.swallaby.foodon.core.util.DateUtil.rememberWeekCount
import com.swallaby.foodon.domain.calendar.model.CalendarItem
import com.swallaby.foodon.domain.calendar.model.CalendarType
import com.swallaby.foodon.presentation.calendar.component.CalendarHeader
import com.swallaby.foodon.presentation.calendar.component.CalendarPager
import com.swallaby.foodon.presentation.calendar.component.TabContentPager
import com.swallaby.foodon.presentation.calendar.component.WeeklyLabel
import com.swallaby.foodon.presentation.calendar.component.WeightBox
import com.swallaby.foodon.presentation.calendar.viewmodel.CalendarViewModel
import kotlinx.coroutines.launch
import org.threeten.bp.YearMonth

@Composable
fun CalendarScreen(
    viewModel: CalendarViewModel = hiltViewModel(),
) {

    val uiState by viewModel.uiState.collectAsState()

    // 날짜 관리
    val today = uiState.today
    val baseYearMonth = remember { YearMonth.from(today) }

    // 탭 상태 관리
    val selectedDate = uiState.selectedDate
    val selectedTabIndex = uiState.selectedTabIndex
    val currentYearMonth = uiState.currentYearMonth

    val calendarType = CalendarType.values()[selectedTabIndex]
    val weekCount = rememberWeekCount(currentYearMonth, today)

    // 캘린더 페이지 관리
    var monthOffset by remember { mutableIntStateOf(0) }

    val nextMonth = currentYearMonth.plusMonths(1)
    val maxPage = if (nextMonth.isAfter(baseYearMonth)) 2 else 3

    val pagerState = rememberPagerState(initialPage = 1, pageCount = { maxPage })
    val scope = rememberCoroutineScope()

    // 날짜와 연관된 데이터 관리
    val calendarItems = (uiState.calendarResult as? ResultState.Success)?.data.orEmpty()

    val calendarItemMap by remember(calendarItems) {
        derivedStateOf {
            calendarItems.associateBy {
                when (it) {
                    is CalendarItem.Meal -> it.data.date
                    is CalendarItem.Weight -> it.data.date
                    is CalendarItem.Recommendation -> it.data.date
                }
            }
        }
    }

    val selectedMeal by remember(calendarItemMap, selectedDate) {
        derivedStateOf {
            calendarItemMap[selectedDate.toString()] as? CalendarItem.Meal
        }
    }

    LaunchedEffect(pagerState.currentPage, pagerState.isScrollInProgress) {
        if (!pagerState.isScrollInProgress && pagerState.currentPage != 1) {
            val delta = pagerState.currentPage - 1
            monthOffset += delta

            val newMonth = baseYearMonth.plusMonths(monthOffset.toLong())
            viewModel.updateYearMonth(newMonth)

            pagerState.scrollToPage(1)
        }
    }

    // 현재 월, 선택 날짜 변경 처리
    LaunchedEffect(currentYearMonth, selectedTabIndex) {
        val isSameMonth = currentYearMonth == baseYearMonth
        viewModel.selectDate(if (isSameMonth) today else currentYearMonth.atDay(1))

        // 추천 탭인 경우에만 기본 선택 주차 세팅
        if (calendarType == CalendarType.RECOMMENDATION) {
            viewModel.selectWeek(if (isSameMonth) getWeekOfMonth(today) else 0)
        }

        viewModel.fetchCalendarData(calendarType, currentYearMonth.toString())
    }

    // 탭 전환 시 추가 데이터 로딩 (하단 콘텐츠)
    LaunchedEffect(selectedTabIndex) {
        when (calendarType) {
            CalendarType.WEIGHT -> viewModel.fetchUserWeight()
            CalendarType.RECOMMENDATION -> viewModel.fetchRecommendFoods(currentYearMonth.toString())
            else -> Unit
        }
    }

    Scaffold(
        floatingActionButton = {
            MonthlyTabBar(
                modifier = Modifier.padding(bottom = 16.dp),
                selectedIndex = selectedTabIndex,
                onTabSelected = viewModel::selectTab
            )
        },
        floatingActionButtonPosition = FabPosition.Center,
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(bottom = 100.dp)
                .fillMaxSize()
        ) {
            CalendarHeader(
                currentYearMonth = currentYearMonth,
                onPreviousMonth = { scope.launch { pagerState.animateScrollToPage(pagerState.currentPage - 1) } },
                onNextMonth = { scope.launch { pagerState.animateScrollToPage(pagerState.currentPage + 1) } }
            )

            WeeklyLabel()

            CalendarPager(
                pagerState = pagerState,
                calendarItemMap = calendarItemMap,
                uiState = uiState,
                onDateSelected = viewModel::selectDate
            )

            Spacer(modifier = Modifier.height(16.dp))

            UnitContent(calendarType)

            Spacer(modifier = Modifier.height(16.dp))

            HorizontalDivider(color = Border025, thickness = 1.dp)

            TabContentPager(
                uiState = uiState,
                selectedMeal = selectedMeal,
                weekCount = weekCount,
                onTabChanged = viewModel::selectTab,
                onWeeklyTabChanged = { weekIndex ->
                    viewModel.fetchRecommendFoods(
                        yearMonth = currentYearMonth.toString(),
                        week = (weekIndex + 1)
                    )

                    viewModel.selectWeek(weekIndex)
                }
            )
        }
    }
}

@Composable
fun UnitContent(calendarType: CalendarType) {
    Box(modifier = Modifier.padding(horizontal = 24.dp)) {
        when (calendarType) {
            CalendarType.MEAL -> {
                Text(
                    text = stringResource(R.string.tab_meal_bottom),
                    style = NotoTypography.NotoMedium13,
                    color = G700
                )
            }

            CalendarType.WEIGHT -> {
                WeightBox(
                    text = stringResource(R.string.tab_weight_bottom)
                )
            }

            else -> Unit
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CalendarPreview() {
    FoodonTheme {
        CalendarScreen()
    }
}
