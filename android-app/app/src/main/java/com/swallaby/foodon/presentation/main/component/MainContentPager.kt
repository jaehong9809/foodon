package com.swallaby.foodon.presentation.main.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.swallaby.foodon.core.result.ResultState
import com.swallaby.foodon.core.ui.theme.BG300
import com.swallaby.foodon.core.ui.theme.WB500
import com.swallaby.foodon.domain.calendar.model.RecommendFood
import com.swallaby.foodon.domain.main.model.GoalManage
import com.swallaby.foodon.domain.main.model.NutrientIntake
import com.swallaby.foodon.domain.main.model.NutrientManage
import com.swallaby.foodon.presentation.main.model.CalendarInfo

@Composable
fun MainContentPager(
    intakeResult: ResultState<NutrientIntake>,
    nutrientManageResult: ResultState<List<NutrientManage>>,
    recommendFoods: ResultState<List<RecommendFood>>,
    goalManageResult: ResultState<GoalManage>,
    calendarInfo: CalendarInfo
) {

    val pagerState = rememberPagerState(initialPage = 0, pageCount = { 4 })

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxWidth()
                .height(350.dp) // 고정 길이 사용
        ) { page ->
            when (page) {
                0 -> NutrientIntakeContent(intakeResult, calendarInfo)
                1 -> NutrientManageContent(nutrientManageResult)
                2 -> RecommendFoodContent(recommendFoods)
                3 -> GoalManageContent(goalManageResult)
            }
        }

        Row(
            modifier = Modifier.padding(top = 16.dp, bottom = 24.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            repeat(pagerState.pageCount) { index ->
                val isSelected = pagerState.currentPage == index
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .background(
                            color = if (isSelected) WB500 else BG300,
                            shape = CircleShape
                        )
                )
            }
        }
    }
}
