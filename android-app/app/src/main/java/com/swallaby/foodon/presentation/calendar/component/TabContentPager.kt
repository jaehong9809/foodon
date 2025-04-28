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
import com.swallaby.foodon.domain.calendar.model.RecommendFood
import com.swallaby.foodon.domain.calendar.model.UserWeight
import kotlinx.coroutines.launch

@Composable
fun TabContentPager(
    modifier: Modifier = Modifier,
    selectedTab: Int,
    selectedItem: CalendarItem?,
    userWeight: ResultState<UserWeight>,
    recommendFoods: ResultState<List<RecommendFood>>,
    weeksInCurrentMonth: Int,
    onTabChanged: (Int) -> Unit,
    onWeeklyTabChanged: (Int) -> Unit
) {

    val pagerState = rememberPagerState(initialPage = selectedTab, pageCount = { 3 })
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
                    if (selectedItem is CalendarItem.Meal) {
                        MealContent(calendarMeal = selectedItem.data)
                    }
                }
                1 -> {
                    when (userWeight) {
                        is ResultState.Success -> {
                            WeightContent(userWeight = userWeight.data)
                        }
                        else -> {}
                    }
                }
                2 -> {
                    when (recommendFoods) {
                        is ResultState.Success -> {
                            RecommendationContent(
                                weeksInCurrentMonth = weeksInCurrentMonth,
                                recommendFoods = recommendFoods.data,
                                onWeeklyTabChanged = {
                                    onWeeklyTabChanged(it)
                                }
                            )
                        }
                        else -> {}
                    }
                }
            }
        }
    }

    // 스와이프하면 탭 변경
    LaunchedEffect(pagerState.currentPage) {
        if (selectedTab != pagerState.currentPage) {
            onTabChanged(pagerState.currentPage)
        }
    }

    // 탭 직접 변경 시 페이지도 이동
    LaunchedEffect(selectedTab) {
        if (selectedTab != pagerState.currentPage) {
            scope.launch {
                pagerState.scrollToPage(selectedTab)
            }
        }
    }

}
