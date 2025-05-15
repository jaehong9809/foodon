package com.swallaby.foodon.presentation.calendar.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.swallaby.foodon.core.result.ResultState
import com.swallaby.foodon.domain.calendar.model.CalendarItem
import com.swallaby.foodon.domain.calendar.model.CalendarMeal
import com.swallaby.foodon.domain.calendar.model.RecommendFood
import com.swallaby.foodon.domain.calendar.model.UserWeight
import com.swallaby.foodon.presentation.calendar.model.CalendarStatus
import kotlinx.coroutines.launch

@Composable
fun TabContentPager(
    modifier: Modifier = Modifier,
    selectedMeal: CalendarItem?,
    weightResult: ResultState<UserWeight>,
    recommendFoods: ResultState<List<RecommendFood>>,
    calendarStatus: CalendarStatus,
    onTabChanged: (Int) -> Unit,
    onWeeklyTabChanged: (Int) -> Unit,
    onUpdateWeight: () -> Unit
) {

    val selectedTabIndex = calendarStatus.selectedTabIndex

    val pagerState = rememberPagerState(initialPage = selectedTabIndex, pageCount = { 3 })
    val scope = rememberCoroutineScope()

    HorizontalPager(
        state = pagerState,
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
    ) { page ->
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.TopStart
        ) {
            when (page) {
                0 -> {
                    val meal = (selectedMeal as? CalendarItem.Meal)?.data ?: CalendarMeal()
                    MealContent(calendarMeal = meal)
                }
                1 -> {
                    WeightContent(weightResult, onUpdateWeight)
                }
                2 -> {
                    RecommendationContent(
                        calendarStatus = calendarStatus,
                        recommendFoods = recommendFoods,
                        onWeeklyTabChanged = {
                            onWeeklyTabChanged(it)
                        }
                    )
                }
            }
        }
    }

    // 스와이프하면 탭 변경
    LaunchedEffect(pagerState.currentPage) {
        if (selectedTabIndex != pagerState.currentPage) {
            onTabChanged(pagerState.currentPage)
        }
    }

    // 탭 직접 변경 시 페이지도 이동
    LaunchedEffect(selectedTabIndex) {
        if (selectedTabIndex != pagerState.currentPage) {
            scope.launch {
                pagerState.scrollToPage(selectedTabIndex)
            }
        }
    }

}
