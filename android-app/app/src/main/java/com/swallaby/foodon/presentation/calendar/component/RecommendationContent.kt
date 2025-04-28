package com.swallaby.foodon.presentation.calendar.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.swallaby.foodon.R
import com.swallaby.foodon.core.ui.component.WeekTabBar
import com.swallaby.foodon.core.ui.theme.Bkg04
import com.swallaby.foodon.domain.calendar.model.RecommendFood

@Composable
fun RecommendationContent(
    weeksInCurrentMonth: Int,
    recommendFoods: List<RecommendFood> = emptyList(),
    onWeeklyTabChanged: (Int) -> Unit
) {
    var selectedWeek by remember { mutableIntStateOf(0) }

    Column {
        WeekTabBar(
            weeks = (1..weeksInCurrentMonth).map { stringResource(R.string.tab_weekly, it) },
            selectedIndex = selectedWeek,
            onTabSelected = {
                selectedWeek = it
                onWeeklyTabChanged(selectedWeek)
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        TabContentLayout(
            title = stringResource(R.string.tab_content_title_recommend),
            bgColor = Bkg04
        ) {

            Spacer(modifier = Modifier.height(4.dp))

            Column(
                modifier = Modifier
                    .wrapContentHeight(),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                recommendFoods.forEach { item ->
                    RecommendFoodCompact(item)
                }
            }

        }
    }
}

@Preview(showBackground = true)
@Composable
fun RecommendationPreview() {
    RecommendationContent(weeksInCurrentMonth = 4, onWeeklyTabChanged = {})
}