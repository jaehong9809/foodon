package com.swallaby.foodon.presentation.main


import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.FabPosition
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.swallaby.foodon.R
import com.swallaby.foodon.core.result.ResultState
import com.swallaby.foodon.core.ui.component.FloatingButton
import com.swallaby.foodon.core.ui.theme.Bkg04
import com.swallaby.foodon.core.ui.theme.FoodonTheme
import com.swallaby.foodon.core.util.DateUtil.getWeekOfMonth
import com.swallaby.foodon.core.util.toCalendarItemMap
import com.swallaby.foodon.presentation.calendar.component.WeeklyLabel
import com.swallaby.foodon.presentation.main.component.MainCalendarHeader
import com.swallaby.foodon.presentation.main.component.MainCalendarPager
import com.swallaby.foodon.presentation.main.component.MainContentPager
import com.swallaby.foodon.presentation.main.component.MealRecordContent
import com.swallaby.foodon.presentation.main.model.CalendarInfo
import com.swallaby.foodon.presentation.main.viewmodel.MainViewModel
import com.swallaby.foodon.presentation.navigation.NavRoutes
import com.swallaby.foodon.presentation.nutritionedit.component.NutrientField
import kotlinx.coroutines.launch
import org.threeten.bp.LocalDate
import org.threeten.bp.YearMonth
import kotlin.math.min

@Composable
fun MainScreen(
    viewModel: MainViewModel = hiltViewModel(),
    onMonthlyClick: () -> Unit = {},
    onRecordClick: () -> Unit = {},
    onMealClick: (Long) -> Unit = {},
    onClickNavigate: (NavRoutes) -> Unit = {}
) {

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val sharedState = viewModel.calendarSharedState

    val calendarInfo = CalendarInfo(
        today = LocalDate.now(),
        selectedDate = sharedState.selectedDate.collectAsStateWithLifecycle().value,
        currentYearMonth = sharedState.currentYearMonth.collectAsStateWithLifecycle().value,
        currentWeekStart = sharedState.currentWeekStart.collectAsStateWithLifecycle().value
    )

    val calendarItems = (uiState.mealCalendarResult as? ResultState.Success)?.data.orEmpty()

    val mealItemMap by remember(calendarItems) {
        derivedStateOf { calendarItems.toCalendarItemMap() }
    }

    val nextWeekStart = calendarInfo.currentWeekStart.plusWeeks(1)
    val maxPage = when {
        nextWeekStart.isAfter(calendarInfo.today) -> 2
        else -> 3
    }

    val pagerState = rememberPagerState(initialPage = 1, pageCount = { maxPage })
    val scope = rememberCoroutineScope()

    var value by remember { mutableStateOf("") }

    LaunchedEffect(pagerState.currentPage, pagerState.isScrollInProgress) {
        if (!pagerState.isScrollInProgress && pagerState.currentPage != 1) {
            val delta = pagerState.currentPage - 1

            sharedState.goToWeek(delta)
            pagerState.scrollToPage(1)
        }
    }

    LaunchedEffect(calendarInfo.selectedDate) {
        viewModel.updateDailyData(calendarInfo.selectedDate)
    }

    LaunchedEffect(calendarInfo.currentYearMonth) {
        viewModel.fetchCalendarData(calendarInfo.currentYearMonth)
    }

    LaunchedEffect(calendarInfo.currentWeekStart) {
        viewModel.fetchRecommendation(
            YearMonth.from(calendarInfo.currentWeekStart),
            getWeekOfMonth(calendarInfo.currentWeekStart)
        )
    }

    Scaffold(
        floatingActionButton = {
            FloatingButton(
                modifier = Modifier,
                icon = R.drawable.icon_ai_camera,
                text = stringResource(R.string.btn_record)
            ) {
                onRecordClick()
            }
        },
        floatingActionButtonPosition = FabPosition.End,
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(bottom = 100.dp)
                .fillMaxSize()
        ) {

            MainCalendarHeader(
                currentYearMonth = calendarInfo.currentYearMonth,
                onMonthlyClick = onMonthlyClick,
                onTodayClick = {
                    sharedState.resetToTodayWeek()

                    scope.launch {
                        pagerState.scrollToPage(1)
                    }
                }
            )

            WeeklyLabel()

            MainCalendarPager(
                pagerState = pagerState,
                mealItemMap = mealItemMap,
                calendarInfo = calendarInfo,
                onDateSelected = { date ->
                    sharedState.updateDate(date)
                }
            )

            HorizontalDivider(thickness = 1.dp, color = Bkg04)

            MainContentPager(
                intakeResult = uiState.intakeResult,
                nutrientManageResult = uiState.nutrientManageResult,
                recommendFoods = sharedState.recommendFoods.collectAsStateWithLifecycle().value,
                goalManageResult = uiState.goalManageResult,
                calendarInfo = calendarInfo,
                onClickNavigate = onClickNavigate
            )

            HorizontalDivider(thickness = 8.dp, color = Bkg04)

            MealRecordContent(
                uiState.recordResult,
                calendarInfo
            ) { mealId ->
                onMealClick(mealId)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MainPreview() {
    FoodonTheme {
        MainScreen()
    }
}