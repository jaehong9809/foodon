package com.swallaby.foodon.presentation.calendar.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.swallaby.foodon.R
import com.swallaby.foodon.core.result.ResultState
import com.swallaby.foodon.core.ui.component.WeekTabBar
import com.swallaby.foodon.core.ui.theme.Bkg04
import com.swallaby.foodon.core.ui.theme.G700
import com.swallaby.foodon.core.ui.theme.font.NotoTypography
import com.swallaby.foodon.domain.calendar.model.RecommendFood
import com.swallaby.foodon.presentation.calendar.model.CalendarStatus

@Composable
fun RecommendationContent(
    calendarStatus: CalendarStatus,
    recommendFoods: ResultState<List<RecommendFood>>,
    onWeeklyTabChanged: (Int) -> Unit
) {
    var selectedWeek by remember { mutableIntStateOf(0) }

    // 달이 바뀔 때마다 선택된 week 초기화
    LaunchedEffect(calendarStatus.selectedWeekIndex) {
        selectedWeek = calendarStatus.selectedWeekIndex
    }

    val foods = (recommendFoods as? ResultState.Success)?.data.orEmpty()

    Column(
        modifier = Modifier.padding(vertical = 16.dp)
    ) {
        WeekTabBar(
            weeks = (1..calendarStatus.weekCount).map { stringResource(R.string.tab_weekly, it) },
            selectedIndex = selectedWeek,
            onTabSelected = {
                selectedWeek = it
                onWeeklyTabChanged(selectedWeek)
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        TabContentLayout(
            modifier = Modifier.padding(horizontal = 24.dp),
            title = stringResource(R.string.tab_content_title_recommend),
            bgColor = Bkg04
        ) {

            Spacer(modifier = Modifier.height(4.dp))

            if (foods.isEmpty()) {
                Text(
                    text = stringResource(R.string.main_recommend_food_empty),
                    style = NotoTypography.NotoMedium14.copy(color = G700),
                    textAlign = TextAlign.Start
                )
            }

            if (foods.isNotEmpty()) {
                Column(
                    modifier = Modifier.wrapContentHeight(),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    foods.forEach { item ->
                        RecommendFoodCompact(item)
                    }
                }
            }

        }
    }
}

@Preview(showBackground = true)
@Composable
fun RecommendationPreview() {
//    RecommendationContent(
//        weekCount = 4,
//        selectedWeekIndex = 0,
//        recommendFoods = ResultState.Loading,
//        onWeeklyTabChanged = {})
}