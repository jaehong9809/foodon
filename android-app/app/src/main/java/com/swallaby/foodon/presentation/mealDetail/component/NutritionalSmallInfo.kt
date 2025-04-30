package com.swallaby.foodon.presentation.mealDetail.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.swallaby.foodon.core.ui.theme.G700
import com.swallaby.foodon.core.ui.theme.font.SpoqaTypography
import com.swallaby.foodon.core.util.StringUtil
import com.swallaby.foodon.domain.food.model.Nutrition

@Composable
fun NutritionalSmallInfo(
    modifier: Modifier = Modifier,
    nutrition: Nutrition
) {
    Row {
        Box(
            modifier = modifier
                .height(16.dp)
                .aspectRatio(1f)
                .background(
                    color = nutrition.nutritionType.color, shape = RoundedCornerShape(2.dp)
                ),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                nutrition.nutritionType.displayName.slice(IntRange(0, 0)),
                style = SpoqaTypography.SpoqaBold9.copy(color = Color.White)
            )
        }
        Spacer(modifier.width(4.dp))
        Text(
            text = StringUtil.formatNutrition(nutrition.amount),
            style = SpoqaTypography.SpoqaMedium13.copy(color = G700)
        )

    }
}

@Preview(showBackground = true)
@Composable
fun NutritionalSmallInfoPreview() {
    NutritionalSmallInfo(nutrition = Nutrition())
}