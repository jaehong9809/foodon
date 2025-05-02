package com.swallaby.foodon.presentation.mealDetail.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.swallaby.foodon.R
import com.swallaby.foodon.core.ui.theme.Bkg03
import com.swallaby.foodon.core.ui.theme.G700
import com.swallaby.foodon.core.ui.theme.G900
import com.swallaby.foodon.core.ui.theme.font.SpoqaTypography.SpoqaBold24
import com.swallaby.foodon.core.ui.theme.font.SpoqaTypography.SpoqaMedium16
import com.swallaby.foodon.core.util.StringUtil
import com.swallaby.foodon.domain.food.model.MealType
import com.swallaby.foodon.domain.food.model.Nutrition
import com.swallaby.foodon.domain.food.model.NutritionType


@Composable
fun NutritionalIngredientsComponent(
    modifier: Modifier,
    mealType: MealType,
    mealTime: String,
    totalCarbs: Int,
    totalFat: Int,
    totalKcal: Int,
    totalProtein: Int,
    onMealTypeClick: () -> Unit = {},
    onTimeClick: () -> Unit = {},
) {
    val totalNutrition = totalCarbs + totalProtein + totalFat

    val nutritions = listOf(
        Nutrition(NutritionType.CARBOHYDRATE, totalCarbs, totalCarbs.toFloat() / totalNutrition),
        Nutrition(NutritionType.PROTEIN, totalProtein, totalProtein.toFloat() / totalNutrition),
        Nutrition(NutritionType.FAT, totalFat, totalFat.toFloat() / totalNutrition),
    ).sortedByDescending { it.amount }

    Box(
        modifier = modifier
            .background(color = Color.White)
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(24.dp),
    ) {
        Column {
            MealTime(
                modifier,
                mealType,
                mealTime,
                onMealTypeClick = onMealTypeClick,
                onTimeClick = onTimeClick
            )
            Spacer(modifier.height(24.dp))
            Row(
                modifier = modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    stringResource(R.string.total_intake), style = SpoqaMedium16.copy(color = G900)
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = StringUtil.formatKcal(totalKcal),
                        style = SpoqaBold24.copy(color = G900)
                    )
                    Spacer(modifier = modifier.width(2.dp))
                    Text("kal", style = SpoqaMedium16.copy(color = G700))
                }
            }
            Spacer(modifier.height(16.dp))
            Box(
                modifier = modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .background(color = Bkg03, shape = RoundedCornerShape(10.dp))
                    .padding(16.dp)
            ) {
                Column(modifier = modifier.fillMaxWidth()) {
                    NutritionalIngredientPercentage(
                        modifier = modifier, nutritions = nutritions,
                    )
                    Spacer(modifier = modifier.height(16.dp))
                    Row(
                        modifier = modifier
                            .fillMaxWidth()
                            .wrapContentHeight(),
                    ) {
                        repeat(nutritions.size, action = { index ->
                            NutritionalMediumInfo(
                                modifier = modifier.weight(1f),
                                nutritionType = nutritions[index].nutritionType,
                                amount = nutritions[index].amount
                            )
                        })
                    }
                }
            }

        }
    }
}

@Composable
private fun MealTime(
    modifier: Modifier,
    mealType: MealType,
    mealTime: String,
    onMealTypeClick: () -> Unit = {},
    onTimeClick: () -> Unit,
) {
    Row(modifier = modifier.fillMaxWidth()) {
        DropButton(
            modifier = modifier
                .wrapContentWidth()
                .height(32.dp),
            onClick = onMealTypeClick,
            text = mealType.displayName,
            suffixIcon = {
                Image(
                    painter = painterResource(R.drawable.icon_down_chevron),
                    contentDescription = "down_chevron"
                )
            },
        )
        Spacer(modifier = modifier.width(6.dp))
        DropButton(
            modifier = modifier
                .wrapContentWidth()
                .height(32.dp),
            onClick = onTimeClick,
            text = mealTime,
            prefixIcon = {
                Image(
                    modifier = modifier.size(12.dp),
                    painter = painterResource(R.drawable.icon_time),
                    contentDescription = "time"
                )
            },
            suffixIcon = {
                Image(
                    painter = painterResource(R.drawable.icon_down_chevron),
                    contentDescription = "down_chevron"
                )
            },
        )
    }
}


@Preview
@Composable
fun NutritionalIngredientsComponentPreview() {
    NutritionalIngredientsComponent(
        modifier = Modifier,
        mealType = MealType.BREAKFAST,
        mealTime = "12:00",
        totalCarbs = 100,
        totalFat = 100,
        totalKcal = 100,
        totalProtein = 100
    )
}