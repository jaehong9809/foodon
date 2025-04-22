package com.swallaby.foodon.presentation.calendar.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import kotlinx.coroutines.launch

@Composable
fun TabContentPager(
    modifier: Modifier = Modifier,
    selectedTab: Int,
    onTabChanged: (Int) -> Unit
) {

    val pagerState = rememberPagerState(initialPage = selectedTab, pageCount = { 3 })
    val scope = rememberCoroutineScope()

    HorizontalPager(
        state = pagerState,
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
    ) { page ->
        when (page) {
            0 -> MealContent()
            1 -> WeightContent()
            2 -> RecommendationContent()
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
