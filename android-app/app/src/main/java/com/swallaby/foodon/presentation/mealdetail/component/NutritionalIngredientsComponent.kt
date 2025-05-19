package com.swallaby.foodon.presentation.mealdetail.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.swallaby.foodon.R
import com.swallaby.foodon.core.ui.theme.Border02
import com.swallaby.foodon.core.ui.theme.G700
import com.swallaby.foodon.core.ui.theme.G900
import com.swallaby.foodon.core.ui.theme.WB500
import com.swallaby.foodon.core.ui.theme.crop
import com.swallaby.foodon.core.ui.theme.font.NotoTypography
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
    totalCarbs: Double,
    totalFat: Double,
    totalKcal: Int,
    totalProtein: Double,
    enabledUpdate: Boolean,
    onMealTypeClick: (MealType) -> Unit = {},
    onTimeClick: () -> Unit = {},
) {
    val totalNutrition = totalCarbs + totalProtein + totalFat
    val nutritions = listOfNotNull(totalCarbs.takeIf { it > 0 }
        ?.let { Nutrition(NutritionType.CARBOHYDRATE, it, (it / totalNutrition).toFloat()) },
        totalProtein.takeIf { it > 0 }
            ?.let { Nutrition(NutritionType.PROTEIN, it, (it / totalNutrition).toFloat()) },
        totalFat.takeIf { it > 0 }?.let {
            Nutrition(
                NutritionType.FAT, it, (it / totalNutrition).toFloat()
            )
        }).sortedByDescending { it.amount }

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
                enabledUpdate,
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
            NutritionInfoBox(
                modifier = modifier, nutritions = nutritions
            )
        }
    }
}


@Composable
private fun MealTime(
    modifier: Modifier,
    mealType: MealType,
    mealTime: String,
    enabledUpdate: Boolean,
    onMealTypeClick: (MealType) -> Unit = {},
    onTimeClick: () -> Unit,
) {
//    Log.d("mealType", "mealType: $mealType")
    var expanded by remember { mutableStateOf(false) }
    Row(modifier = modifier.fillMaxWidth()) {
        Box {
            DropButton(
                modifier = modifier
                    .wrapContentWidth()
                    .height(32.dp),
                enabled = enabledUpdate,
                onClick = {
                    expanded = true
                },
                text = mealType.displayName,
                suffixIcon = {
                    Image(
                        painter = painterResource(R.drawable.icon_down_chevron),
                        contentDescription = "down_chevron"
                    )
                },
            )
            // 드롭다운 메뉴
            DropdownMenu(modifier = modifier
                .border(
                    shape = RoundedCornerShape(10.dp), color = Border02, width = 1.dp
                )
                // DropdownMenu 의 기본 Vertical Padding 8.dp 를 0.dp 로 변경
                .crop(vertical = 8.dp)
                .background(color = Color.White, shape = RoundedCornerShape(10.dp)),
                expanded = expanded,
                onDismissRequest = { expanded = false }) {
                MealType.values().forEachIndexed { index, item ->
                    DropdownMenuItem(contentPadding = PaddingValues(
                        horizontal = 12.dp, vertical = 0.dp
                    ), modifier = modifier
                        .width(200.dp)
                        .height(48.dp), onClick = {
                        expanded = false
                        onMealTypeClick(item)
                    }, text = {
                        Text(
                            text = item.displayName,
                            style = NotoTypography.NotoNormal16.copy(color = G900)
                        )
                    }, trailingIcon = {
                        if (mealType == item) {
                            Icon(
                                painter = painterResource(R.drawable.icon_check),
                                contentDescription = "check",
                                tint = WB500,
                            )
                        }
                    })
                    // 마지막 아이템이 아닐 경우에만 구분선 추가
                    if (index < MealType.values().size - 1) {
                        HorizontalDivider(
                            color = Border02
                        )
                    }
                }
            }
        }

        Spacer(modifier = modifier.width(6.dp))
        DropButton(
            modifier = modifier
                .wrapContentWidth()
                .height(32.dp),
            enabled = enabledUpdate,
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
        totalCarbs = 100.0,
        totalFat = 100.0,
        totalKcal = 100,
        totalProtein = 100.0,
        enabledUpdate = true,
    )
}