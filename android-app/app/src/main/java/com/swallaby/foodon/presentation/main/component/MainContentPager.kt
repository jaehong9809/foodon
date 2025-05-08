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
import com.swallaby.foodon.core.ui.theme.BG300
import com.swallaby.foodon.core.ui.theme.WB500
import com.swallaby.foodon.presentation.main.viewmodel.MainUiState

@Composable
fun MainContentPager(
    uiState: MainUiState
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
                0 -> NutrientIntakeContent(uiState)
                1 -> NutrientManageContent(uiState.manageResult)
                2 -> RecommendFoodContent(uiState.recommendMealResult)
                3 -> GoalManageContent()
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
