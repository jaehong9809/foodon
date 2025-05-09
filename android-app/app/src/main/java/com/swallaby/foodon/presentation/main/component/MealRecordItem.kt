package com.swallaby.foodon.presentation.main.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.swallaby.foodon.R
import com.swallaby.foodon.core.ui.theme.BG100
import com.swallaby.foodon.core.ui.theme.Border02
import com.swallaby.foodon.core.ui.theme.G700
import com.swallaby.foodon.core.ui.theme.G750
import com.swallaby.foodon.core.ui.theme.G800
import com.swallaby.foodon.core.ui.theme.G900
import com.swallaby.foodon.core.ui.theme.font.NotoTypography
import com.swallaby.foodon.core.ui.theme.font.SpoqaTypography
import com.swallaby.foodon.core.util.StringUtil.formatKcal
import com.swallaby.foodon.domain.food.model.MealType
import com.swallaby.foodon.domain.food.model.Nutrition
import com.swallaby.foodon.domain.food.model.NutritionType
import com.swallaby.foodon.domain.main.model.MealRecord
import com.swallaby.foodon.presentation.mealdetail.component.NutritionalSmallInfo

@Composable
fun MealRecordItem(
    meal: MealRecord,
    onClick: (mealId: Long) -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .border(1.dp, color = Border02, shape = RoundedCornerShape(10.dp))
            .padding(16.dp)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = {
                    onClick(meal.mealId)
                }
            )
    ) {

        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Top
            ) {
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    // 식사 타입
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        MealTypeBox(meal.mealTimeType.displayName)
                        MealTypeBox(meal.mealTime)
                    }

                    Spacer(Modifier.height(2.dp))

                    Text(
                        text = meal.mealItems.joinToString(", "),
                        style = NotoTypography.NotoMedium14.copy(color = G800),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(Modifier.height(20.dp))

                    // 칼로리 정보
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(2.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text(
                            formatKcal(meal.totalKcal),
                            style = SpoqaTypography.SpoqaBold20.copy(color = G900)
                        )
                        Text(
                            stringResource(R.string.main_kcal_unit),
                            style = SpoqaTypography.SpoqaMedium14.copy(color = G700)
                        )
                    }

                }

                if (meal.imageUrl.isNotBlank()) {
                    Spacer(Modifier.width(16.dp))

                    AsyncImage(
                        model = meal.imageUrl,
                        contentDescription = "음식 사진",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .height(80.dp)
                            .width(80.dp)
                            .clip(shape = RoundedCornerShape(6.dp))
                    )
                }
            }

            Spacer(Modifier.height(8.dp))

            val nutrients = listOf(
                Nutrition(nutritionType = NutritionType.CARBOHYDRATE, amount = meal.totalCarbs),
                Nutrition(nutritionType = NutritionType.PROTEIN, amount = meal.totalProtein),
                Nutrition(nutritionType = NutritionType.FAT, amount = meal.totalFat),
            )

            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                nutrients.forEach { nutrient ->
                    NutritionalSmallInfo(
                        nutrition = nutrient
                    )
                }
            }
        }

    }
}

@Composable
fun MealTypeBox(
    text: String,
) {
    Box(
        modifier = Modifier
            .background(BG100, shape = RoundedCornerShape(4.dp))
            .padding(4.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            style = SpoqaTypography.SpoqaMedium12.copy(color = G750),
            textAlign = TextAlign.Center
        )
    }
}

@Preview(showBackground = true)
@Composable
fun MealRecordItemPreview() {
    MealRecordItem(meal = MealRecord(
        mealId = 1,
        mealTimeType = MealType.BREAKFAST,
        mealTime = "08:00",
        imageUrl = "",
        mealItems = listOf(
            "쌀밥",
            "된장찌개",
            "김치",
            "쌀밥",
            "된장찌개",
            "김치",
            "쌀밥",
            "된장찌개",
            "김치",
            "쌀밥",
            "된장찌개",
            "김치",
        ),
        totalKcal = 1000,
        totalCarbs = 10.0,
        totalProtein = 5.0,
        totalFat = 5.0
    ), onClick = {})
}