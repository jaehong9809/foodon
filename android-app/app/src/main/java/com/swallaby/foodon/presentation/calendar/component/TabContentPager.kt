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
import com.swallaby.foodon.domain.calendar.model.CalendarItem
import kotlinx.coroutines.launch

@Composable
fun TabContentPager(
    modifier: Modifier = Modifier,
    selectedTab: Int,
    selectedItem: CalendarItem?,
    onTabChanged: (Int) -> Unit,
    onFetchTabData: (Int) -> Unit
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
            // TODO: 각 화면에서 ViewModel 주입 받아서 데이터 사용
            when (page) {
                0 -> {
                    if (selectedItem is CalendarItem.Meal) {
                        MealContent(calendarMeal = selectedItem.data)
                    }
                }
                1 -> {
                    WeightContent()
                }
                2 -> {
                    RecommendationContent()
                }
            }
        }
    }

    // 스와이프하면 탭 변경
    LaunchedEffect(pagerState.currentPage) {
        if (selectedTab != pagerState.currentPage) {
            onTabChanged(pagerState.currentPage)
            onFetchTabData(pagerState.currentPage)
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
