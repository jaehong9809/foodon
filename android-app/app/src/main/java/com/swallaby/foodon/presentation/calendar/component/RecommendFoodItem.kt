package com.swallaby.foodon.presentation.calendar.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.swallaby.foodon.R
import com.swallaby.foodon.core.ui.component.CommonBox
import com.swallaby.foodon.core.ui.theme.BGGreen
import com.swallaby.foodon.core.ui.theme.Border025
import com.swallaby.foodon.core.ui.theme.FoodonTheme
import com.swallaby.foodon.core.ui.theme.G700
import com.swallaby.foodon.core.ui.theme.G900
import com.swallaby.foodon.core.ui.theme.MainWhite
import com.swallaby.foodon.core.ui.theme.TextGreen
import com.swallaby.foodon.core.ui.theme.font.NotoTypography
import com.swallaby.foodon.core.util.StringUtil.formatKcal
import com.swallaby.foodon.domain.calendar.model.RecommendFood
import com.swallaby.foodon.domain.food.model.NutrientLevel

@Composable
fun RecommendFoodCompact(
    food: RecommendFood = RecommendFood()
) {
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
            Text(
                text = food.name,
                color = G900,
                style = NotoTypography.NotoBold14,
            )

            Spacer(modifier = Modifier.width(8.dp))

            CommonBox(
                content = stringResource(R.string.format_kcal, formatKcal(food.kcal)),
                bgColor = BGGreen,
                textColor = TextGreen,
                horizontalPadding = 4.dp,
                height = 20.dp
            )
        }

    }

}

@Composable
fun RecommendFoodDetail(
    modifier: Modifier = Modifier,
    food: RecommendFood = RecommendFood()
) {
    Column(
        modifier = modifier
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
                Text(
                    text = food.name,
                    color = G900,
                    style = NotoTypography.NotoBold14,
                )

                Spacer(modifier = Modifier.height(4.dp))

                CommonBox(
                    content = stringResource(R.string.format_kcal, formatKcal(food.kcal)),
                    bgColor = BGGreen,
                    textColor = TextGreen,
                    horizontalPadding = 4.dp,
                    height = 20.dp
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Column(
                modifier = Modifier
                    .wrapContentHeight(),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                food.nutrientClaims.forEach { item ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Image(
                            painter = painterResource(R.drawable.icon_red_arrow),
                            contentDescription = null,
                            modifier = Modifier.graphicsLayer(
                                rotationZ = if (item.level == NutrientLevel.LOW) 180f else 0f
                            )
                        )

                        Text(
                            text = item.code.displayName,
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