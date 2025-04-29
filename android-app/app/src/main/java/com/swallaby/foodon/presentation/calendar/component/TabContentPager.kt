package com.swallaby.foodon.presentation.calendar.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.swallaby.foodon.core.result.ResultState
import com.swallaby.foodon.domain.calendar.model.CalendarItem
import com.swallaby.foodon.presentation.calendar.viewmodel.CalendarUiState
import kotlinx.coroutines.launch

@Composable
fun TabContentPager(
    modifier: Modifier = Modifier,
    uiState: CalendarUiState,
    selectedMeal: CalendarItem?,
    weekCount: Int,
    onTabChanged: (Int) -> Unit,
    onWeeklyTabChanged: (Int) -> Unit
) {

    val selectedTabIndex = uiState.selectedTabIndex

    val pagerState = rememberPagerState(initialPage = selectedTabIndex, pageCount = { 3 })
    val scope = rememberCoroutineScope()

    HorizontalPager(
        state = pagerState,
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
    ) { page ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(vertical = 16.dp, horizontal = 24.dp),
            contentAlignment = Alignment.TopStart
        ) {
            when (page) {
                0 -> {
                    if (selectedMeal is CalendarItem.Meal) {
                        MealContent(calendarMeal = selectedMeal.data)
                    }
                }
                1 -> uiState.weightState.takeIf { it is ResultState.Success }?.let {
                    WeightContent(userWeight = (it as ResultState.Success).data)
                }
                2 -> uiState.recommendFoods.takeIf { it is ResultState.Success }?.let {
                    RecommendationContent(
                        weekCount = weekCount,
                        selectedWeekIndex = uiState.selectedWeekIndex,
                        recommendFoods = (it as ResultState.Success).data,
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
