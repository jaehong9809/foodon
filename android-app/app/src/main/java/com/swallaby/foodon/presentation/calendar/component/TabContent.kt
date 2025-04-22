package com.swallaby.foodon.presentation.calendar.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.swallaby.foodon.R
import com.swallaby.foodon.core.ui.component.WeekTabBar
import com.swallaby.foodon.core.ui.theme.BGBlueTransparent
import com.swallaby.foodon.core.ui.theme.Bkg04
import com.swallaby.foodon.core.ui.theme.FoodonTheme
import com.swallaby.foodon.core.ui.theme.G900
import com.swallaby.foodon.core.ui.theme.font.SpoqaTypography

@Composable
fun MealContent() {
    TabContentLayout(
        title = stringResource(R.string.tab_content_title_meal),
        bgColor = Bkg04
    ) {
        // TODO: 하루 목표 칼로리
        Text(
            text = stringResource(R.string.format_kcal, "1,600"),
            color = G900,
            style = SpoqaTypography.SpoqaBold18,
        )
    }
}

@Composable
fun WeightContent() {
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        TabContentLayout(
            modifier = Modifier.weight(1f),
            title = stringResource(R.string.tab_content_title_goal_weight),
            bgColor = Bkg04
        ) {
            // TODO: 목표 체중
            Text(
                text = stringResource(R.string.format_kg, 50),
                color = G900,
                style = SpoqaTypography.SpoqaBold18,
            )
        }

        TabContentLayout(
            modifier = Modifier.weight(1f),
            title = stringResource(R.string.tab_content_title_cur_weight),
            bgColor = BGBlueTransparent,
            icon = R.drawable.icon_cur_weight
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {

                // TODO: 현재 체중
                Text(
                    text = stringResource(R.string.format_kg, 55),
                    color = G900,
                    style = SpoqaTypography.SpoqaBold18,
                )

                Spacer(modifier = Modifier.width(8.dp))

                Image(
                    modifier = Modifier
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null,
                            onClick = {
                                // TODO: 현재 체중 수정 (바텀시트 또는 새로운 화면)
                            }
                        ),
                    painter = painterResource(id = R.drawable.icon_bg_pencil),
                    contentDescription = null,
                )
            }

        }
    }
}

@Composable
fun RecommendationContent() {

    var selectedWeek by remember { mutableIntStateOf(0) }

    Column {
        // TODO: 현재 월의 주까지만 탭 보여주기
        WeekTabBar(
            weeks = (1..5).map { stringResource(R.string.tab_weekly, it) },
            selectedIndex = selectedWeek,
            onTabSelected = { selectedWeek = it }
        )

        Spacer(modifier = Modifier.height(16.dp))

        TabContentLayout(
            title = stringResource(R.string.tab_content_title_recommend),
            bgColor = Bkg04
        ) {

            Spacer(modifier = Modifier.height(4.dp))

            // TODO: 추천 음식 리스트 (LazyColumn Scroll X)
            Text(
                text = stringResource(R.string.format_kcal, "1,600"),
                color = G900,
                style = SpoqaTypography.SpoqaBold18,
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TabContentPreview() {
    FoodonTheme {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            MealContent()

            Spacer(modifier = Modifier.height(12.dp))

            WeightContent()

            Spacer(modifier = Modifier.height(12.dp))

            RecommendationContent()
        }

    }
}
