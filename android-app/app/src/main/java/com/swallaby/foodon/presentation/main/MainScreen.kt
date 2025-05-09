package com.swallaby.foodon.presentation.main

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.swallaby.foodon.BuildConfig
import com.swallaby.foodon.R
import com.swallaby.foodon.core.result.ResultState
import com.swallaby.foodon.core.ui.component.FloatingButton
import com.swallaby.foodon.core.ui.theme.Bkg04
import com.swallaby.foodon.core.ui.theme.FoodonTheme
import com.swallaby.foodon.core.ui.theme.MainWhite
import com.swallaby.foodon.core.ui.theme.WB500
import com.swallaby.foodon.core.ui.theme.font.NotoTypography
import com.swallaby.foodon.core.util.DateUtil.calculateWeeksOfMonth
import com.swallaby.foodon.domain.calendar.model.CalendarItem
import com.swallaby.foodon.presentation.calendar.component.WeeklyLabel
import com.swallaby.foodon.presentation.main.component.MainCalendarHeader
import com.swallaby.foodon.presentation.main.component.MainCalendarPager
import com.swallaby.foodon.presentation.main.component.MainContentPager
import com.swallaby.foodon.presentation.main.component.MealRecordContent
import com.swallaby.foodon.presentation.main.viewmodel.MainViewModel
import com.swallaby.foodon.presentation.navigation.LocalNavController
import com.swallaby.foodon.presentation.navigation.NavRoutes
import kotlinx.coroutines.launch

@Composable
fun MainScreen(
    viewModel: MainViewModel = hiltViewModel(),
    onRecordClick: () -> Unit = {},
) {

    val navController = LocalNavController.current
    val uiState by viewModel.uiState.collectAsState()

    val selectedDate = uiState.selectedDate
    val currentYearMonth = uiState.currentYearMonth

    val calendarItems = (uiState.calendarResult as? ResultState.Success)?.data.orEmpty()

    val mealItemMap by remember(calendarItems) {
        derivedStateOf {
            calendarItems
                .filterIsInstance<CalendarItem.Meal>()
                .associateBy { it.data.date }
        }
    }

    val weeksInMonth = remember(uiState.currentYearMonth) {
        calculateWeeksOfMonth(uiState.currentYearMonth)
    }

    val initialPage = weeksInMonth.indexOfFirst { week ->
        uiState.today in week
    }.coerceAtLeast(0)

    val pagerState = rememberPagerState(initialPage = initialPage, pageCount = { weeksInMonth.size })
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        viewModel.fetchCalendarData(currentYearMonth.toString())

        viewModel.fetchRecommendFoods(
            yearMonth = currentYearMonth.toString(),
            week = 1
        )
    }

    LaunchedEffect(selectedDate) {
        viewModel.fetchRecordData(selectedDate.toString())
        viewModel.fetchIntakeData(selectedDate.toString())
        viewModel.fetchManageData(selectedDate.toString())
    }

    LaunchedEffect(pagerState.currentPage) {
        viewModel.fetchRecommendFoods(
            yearMonth = currentYearMonth.toString(),
            week = (pagerState.currentPage) + 1
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
                currentYearMonth = currentYearMonth,
                onMonthlyClick = {
                    navController.navigate(NavRoutes.Calendar.route)
                },
                onTodayClick = {
                    viewModel.selectDate(uiState.today)

                    scope.launch {
                        pagerState.scrollToPage(initialPage)
                    }
                }
            )

            WeeklyLabel()

            MainCalendarPager(
                pagerState = pagerState,
                weeksInMonth = weeksInMonth,
                mealItemMap = mealItemMap,
                uiState = uiState,
                onDateSelected = viewModel::selectDate
            )

            HorizontalDivider(thickness = 1.dp, color = Bkg04)

            MainContentPager(uiState)

            HorizontalDivider(thickness = 8.dp, color = Bkg04)

            MealRecordContent(uiState = uiState) { mealId ->
                navController.navigate(NavRoutes.FoodGraph.MealDetail)
            }

            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(modifier = Modifier
                    .background(WB500)
                    .clickable {
                        navController.navigate(NavRoutes.LoginGraph.route)
                    }) {
                    Text(
                        modifier = Modifier.padding(16.dp),
                        text = "로그인 테스트",
                        color = MainWhite,
                        style = NotoTypography.NotoMedium20
                    )
                }
            }

            if (BuildConfig.DEBUG) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .background(WB500)
                            .clickable {
                                navController.navigate(NavRoutes.Debug.route)
                            }
                    ) {
                        Text(
                            modifier = Modifier.padding(16.dp),
                            text = "디버그 중 동작 - DB",
                            color = MainWhite,
                            style = NotoTypography.NotoMedium20
                        )
                    }
                }
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