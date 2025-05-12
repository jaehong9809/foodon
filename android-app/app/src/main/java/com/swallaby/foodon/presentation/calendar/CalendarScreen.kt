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
    val selectedDate = uiState.selectedDate

    // 탭 상태 관리
    val selectedTabIndex = uiState.selectedTabIndex
    val currentYearMonth = uiState.currentYearMonth

    val calendarType = CalendarType.values()[selectedTabIndex]
    val weekCount = rememberWeekCount(currentYearMonth, today)

    // 캘린더 페이지 관리
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
            val newMonth = currentYearMonth.plusMonths(delta.toLong())

            viewModel.updateYearMonth(newMonth)
            pagerState.scrollToPage(1)
        }
    }

    LaunchedEffect(Unit) {
        viewModel.updateInitialLoaded(false)
    }

    val previousTabIndex = remember { mutableIntStateOf(selectedTabIndex) }

    LaunchedEffect(currentYearMonth, selectedTabIndex) {
        val isTabChanged = selectedTabIndex != previousTabIndex.intValue
        previousTabIndex.intValue = selectedTabIndex

        viewModel.updateCalendarData(calendarType, isTabChanged = isTabChanged)
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
                    viewModel.updateRecommendation(weekIndex)
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
