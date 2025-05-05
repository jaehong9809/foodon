package com.swallaby.foodon.presentation.main.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.swallaby.foodon.R
import com.swallaby.foodon.core.result.ResultState
import com.swallaby.foodon.core.ui.theme.BG300
import com.swallaby.foodon.core.ui.theme.G700
import com.swallaby.foodon.core.ui.theme.G900
import com.swallaby.foodon.core.ui.theme.WB500
import com.swallaby.foodon.core.ui.theme.font.NotoTypography
import com.swallaby.foodon.domain.calendar.model.RecommendFood
import com.swallaby.foodon.presentation.calendar.component.RecommendFoodDetail

@Composable
fun RecommendFoodContent(
    recommendMealResult: ResultState<List<RecommendFood>>
) {

    val meals = (recommendMealResult as? ResultState.Success)?.data.orEmpty()

    Column(
        modifier = Modifier.fillMaxSize().padding(top = 24.dp, start = 24.dp, end = 24.dp),
        verticalArrangement = Arrangement.Top
    ) {
        Text(
            text = stringResource(R.string.main_food_recommend_title),
            color = G900,
            style = NotoTypography.NotoBold18
        )

        // 2개보다 많아질 경우 2개씩 표시
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            for (rowItems in meals.chunked(2)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    rowItems.forEach {
                        RecommendFoodDetail(modifier = Modifier.weight(1f), it)
                    }
                }
            }
        }

        val reasons = meals.map { it.reason }
        RecommendReason(reasons)
    }
}

@Composable
fun RecommendReason(
    reasons: List<String> = emptyList()
) {
    val columnHeight = remember { mutableIntStateOf(0) }

    Column(modifier = Modifier.padding(top = 16.dp)) {
        Text(
            text = stringResource(R.string.main_food_recommend_content),
            color = WB500,
            style = NotoTypography.NotoBold14
        )

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            // 왼쪽 세로 바
            Box(
                modifier = Modifier
                    .width(3.dp)
                    .height(with(LocalDensity.current) { columnHeight.intValue.toDp() })
                    .background(BG300, shape = RoundedCornerShape(100.dp))
            )

            // 추천 이유
            Column(
                modifier = Modifier.onGloballyPositioned { coordinates ->
                    columnHeight.intValue = coordinates.size.height
                },
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                reasons.forEach { content ->
                    Box(modifier = Modifier.padding(bottom = 3.dp)) {
                        Text(
                            text = content,
                            color = G700,
                            style = NotoTypography.NotoMedium14
                        )
                    }
                }
            }
        }
    }
}
