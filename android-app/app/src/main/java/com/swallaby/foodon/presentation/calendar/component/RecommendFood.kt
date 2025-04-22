package com.swallaby.foodon.presentation.calendar.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.swallaby.foodon.R
import com.swallaby.foodon.core.ui.theme.BGKcal
import com.swallaby.foodon.core.ui.theme.Border025
import com.swallaby.foodon.core.ui.theme.FoodonTheme
import com.swallaby.foodon.core.ui.theme.G700
import com.swallaby.foodon.core.ui.theme.G900
import com.swallaby.foodon.core.ui.theme.MainWhite
import com.swallaby.foodon.core.ui.theme.TextKcal
import com.swallaby.foodon.core.ui.theme.font.NotoTypography
import com.swallaby.foodon.core.ui.theme.font.SpoqaTypography

@Composable
fun KcalBox(kcal: String) {
    Column(
        modifier = Modifier
            .wrapContentSize()
            .background(color = BGKcal, shape = RoundedCornerShape(4.dp))
            .padding(4.dp)
    ) {
        Text(
            text = stringResource(R.string.format_kcal, kcal),
            color = TextKcal,
            style = SpoqaTypography.SpoqaMedium12,
        )
    }
}

@Composable
fun RecommendFoodCompact() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = MainWhite, shape = RoundedCornerShape(4.dp))
            .padding(vertical = 8.dp, horizontal = 12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // TODO: 각 음식 이름
            Text(
                text = "병아리콩 커리",
                color = G900,
                style = NotoTypography.NotoBold14,
            )

            Spacer(modifier = Modifier.width(8.dp))

            // TODO: 각 음식의 칼로리
            KcalBox("600")
        }

    }

}

@Composable
fun RecommendFoodDetail() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = MainWhite, shape = RoundedCornerShape(10.dp))
            .border(1.dp, color = Border025, shape = RoundedCornerShape(10.dp))
            .padding(vertical = 14.dp, horizontal = 16.dp)
    ) {
        Column(
            modifier = Modifier.heightIn(min = 132.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column{
                // TODO: 각 음식 이름
                Text(
                    text = "병아리콩 커리",
                    color = G900,
                    style = NotoTypography.NotoBold14,
                )

                Spacer(modifier = Modifier.height(4.dp))

                // TODO: 각 음식의 칼로리
                KcalBox("600")
            }

            Spacer(modifier = Modifier.height(16.dp))

            // TODO: 음식 먹으면 좋은 점 리스트
            val itemsList = listOf("포만감", "배고픔", "만족도")

            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .then(Modifier.scrollable(rememberScrollState(), Orientation.Vertical)),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                items(itemsList) { item ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Image(
                            painter = painterResource(R.drawable.icon_red_arrow),
                            contentDescription = null
                        )

                        Text(
                            text = item,
                            color = G700,
                            style = NotoTypography.NotoMedium13,
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun RecommendFoodPreview() {
    FoodonTheme {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            RecommendFoodCompact()

            Spacer(modifier = Modifier.height(12.dp))

            RecommendFoodDetail()
        }

    }
}