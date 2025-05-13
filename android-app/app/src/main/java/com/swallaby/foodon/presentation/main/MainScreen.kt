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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.swallaby.foodon.R
import com.swallaby.foodon.core.result.ResultState
import com.swallaby.foodon.core.ui.component.FloatingButton
import com.swallaby.foodon.core.ui.theme.Bkg04
import com.swallaby.foodon.core.ui.theme.FoodonTheme
import com.swallaby.foodon.core.ui.theme.MainWhite
import com.swallaby.foodon.core.ui.theme.WB500
import com.swallaby.foodon.core.ui.theme.font.NotoTypography
import com.swallaby.foodon.domain.calendar.model.CalendarItem
import com.swallaby.foodon.domain.calendar.model.CalendarType
import com.swallaby.foodon.presentation.calendar.component.WeeklyLabel
import com.swallaby.foodon.presentation.calendar.viewmodel.CalendarViewModel
import com.swallaby.foodon.presentation.main.component.MainCalendarHeader
import com.swallaby.foodon.presentation.main.component.MainCalendarPager
import com.swallaby.foodon.presentation.main.component.MainContentPager
import com.swallaby.foodon.presentation.main.component.MealRecordContent
import com.swallaby.foodon.presentation.main.viewmodel.MainViewModel
import com.swallaby.foodon.presentation.navigation.LocalNavController
import com.swallaby.foodon.presentation.navigation.NavRoutes
import kotlinx.coroutines.launch
import org.threeten.bp.DayOfWeek
import org.threeten.bp.YearMonth
import org.threeten.bp.temporal.WeekFields

@Composable
fun MainScreen(
    mainViewModel: MainViewModel = hiltViewModel(),
    calendarViewModel: CalendarViewModel = hiltViewModel(),
    onRecordClick: () -> Unit = {}
) {

    val navController = LocalNavController.current

    val mainUiState by mainViewModel.uiState.collectAsState()
    val calendarUiState by calendarViewModel.uiState.collectAsState()

    val selectedDate = calendarUiState.selectedDate
    val currentYearMonth = calendarUiState.currentYearMonth

    val calendarItems = (calendarUiState.calendarResult as? ResultState.Success)?.data.orEmpty()

    val mealItemMap by remember(calendarItems) {
        derivedStateOf {
            calendarItems
                .filterIsInstance<CalendarItem.Meal>()
                .associateBy { it.data.date }
        }
    }

    val weekFields = WeekFields.of(DayOfWeek.SUNDAY, 1)

    val currentWeekStart = calendarViewModel.currentWeekStart
    val weekOfMonth = currentWeekStart.get(weekFields.weekOfMonth())

    val nextWeekStart = currentWeekStart.plusWeeks(1)
    val maxPage = when {
        nextWeekStart.isAfter(calendarUiState.today) -> 2
        else -> 3
    }

    val pagerState = rememberPagerState(initialPage = 1, pageCount = { maxPage })
    val scope = rememberCoroutineScope()

    var value by remember { mutableStateOf("") }

    LaunchedEffect(selectedDate) {
        mainViewModel.updateDailyData(selectedDate)
    }

    LaunchedEffect(pagerState.currentPage, pagerState.isScrollInProgress) {
        if (!pagerState.isScrollInProgress && pagerState.currentPage != 1) {
            val delta = pagerState.currentPage - 1

            calendarViewModel.goToWeek(delta)
            pagerState.scrollToPage(1)
        }
    }

    LaunchedEffect(currentYearMonth) {
        calendarViewModel.fetchCalendarData(CalendarType.MEAL, currentYearMonth)
    }

    LaunchedEffect(currentWeekStart) {
        calendarViewModel.fetchRecommendFoods(
            yearMonth = currentYearMonth,
            week = weekOfMonth
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
                    calendarViewModel.resetToTodayWeek()

                    scope.launch {
                        pagerState.scrollToPage(1)
                    }
                }
            )

            WeeklyLabel()

            MainCalendarPager(
                pagerState = pagerState,
                currentWeekStart = currentWeekStart,
                mealItemMap = mealItemMap,
                calendarUiState = calendarUiState,
                onDateSelected = { date ->
                    calendarViewModel.selectDate(date)
                    calendarViewModel.updateYearMonth(YearMonth.from(date))
                }
            )

            HorizontalDivider(thickness = 1.dp, color = Bkg04)

            // todo formfield 테스트 용으로 넣어놔서 나중에 지우겠습니다!
//            NutrientField(
//                modifier = Modifier.height(100.dp),
//                value = value,
//                onValueChange = { newValue ->
//                    Log.d("NutrientField", "newValue: $newValue")
//                    value = cleanDoubleInput(newValue)
//                    Log.d("NutrientField", "value: $value")
//                },
//                nutrient = "탄수화물",
//                unit = "g",
//            )

            MainContentPager(mainUiState, calendarUiState)

            HorizontalDivider(thickness = 8.dp, color = Bkg04)

            MealRecordContent(
                mainUiState = mainUiState,
                calendarUiState = calendarUiState
            ) { mealId ->
                navController.navigate(NavRoutes.FoodGraph.MealDetail.createRoute(mealId))
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