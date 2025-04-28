package com.swallaby.foodon.presentation.calendar

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.FabPosition
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
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
import com.swallaby.foodon.presentation.calendar.component.*
import com.swallaby.foodon.presentation.calendar.viewmodel.CalendarViewModel
import kotlinx.coroutines.launch
import org.threeten.bp.LocalDate
import org.threeten.bp.YearMonth

@Composable
fun CalendarScreen(
    viewModel: CalendarViewModel = viewModel(),
) {
    // 날짜 관리
    val today = remember { LocalDate.now() }
    val baseYearMonth = remember { YearMonth.from(today) }

    // 캘린더 페이지 관리
    val monthOffsetRange = -12..12
    val pagerState = rememberPagerState(initialPage = 12, pageCount = { monthOffsetRange.count() })
    val scope = rememberCoroutineScope()

    // 탭 상태 관리
    var currentYearMonth by remember { mutableStateOf(baseYearMonth) }
    var selectedTabIndex by remember { mutableIntStateOf(0) }
    var selectedWeekIndex by remember { mutableIntStateOf(0) }

    val calendarType = CalendarType.values()[selectedTabIndex]
    val weekCount = rememberWeekCount(currentYearMonth, today)

    // 날짜와 연관된 데이터 관리
    val uiState by viewModel.uiState.collectAsState()
    val selectedDate = uiState.selectedDate
    val calendarItems = (uiState.calendarState as? ResultState.Success)?.data.orEmpty()
    val calendarItemMap = remember(calendarItems) {
        calendarItems.associateBy {
            when (it) {
                is CalendarItem.Meal -> it.data.date
                is CalendarItem.Weight -> it.data.date
                is CalendarItem.Recommendation -> it.data.date
            }
        }
    }
    val selectedMeal = calendarItemMap[selectedDate.toString()]

    // 현재 월, 선택 날짜 변경 처리
    LaunchedEffect(pagerState.currentPage, selectedTabIndex) {
        currentYearMonth = baseYearMonth.plusMonths((pagerState.currentPage - 12).toLong())

        val isSameMonth = currentYearMonth == baseYearMonth
        viewModel.selectDate(if (isSameMonth) today else currentYearMonth.atDay(1)) // 기본 선택 날짜 세팅

        // 추천 탭인 경우에만 기본 선택 주차 세팅
        if (calendarType == CalendarType.RECOMMENDATION) {
            selectedWeekIndex = if (isSameMonth) (today.dayOfMonth - 1) / 7 + 1 else 0
        }

        // 캘린더 데이터
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
                onTabSelected = { selectedTabIndex = it }
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
                calendarType = calendarType,
                currentYearMonth = currentYearMonth,
                calendarItemMap = calendarItemMap,
                uiState = uiState,
                today = today,
                selectedWeekIndex = selectedWeekIndex,
                onDateSelected = viewModel::selectDate
            )

            Spacer(modifier = Modifier.height(16.dp))

            UnitContent(calendarType)

            Spacer(modifier = Modifier.height(16.dp))

            HorizontalDivider(color = Border025, thickness = 1.dp)

            TabContentPager(
                selectedTab = selectedTabIndex,
                selectedMeal = selectedMeal,
                uiState = uiState,
                weekCount = weekCount,
                selectedWeekIndex = selectedWeekIndex,
                onTabChanged = { selectedTabIndex = it },
                onWeeklyTabChanged = { weekIndex ->
                    viewModel.fetchRecommendFoods(
                        yearMonth = currentYearMonth.toString(),
                        week = (weekIndex + 1)
                    )
                    selectedWeekIndex = weekIndex
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
